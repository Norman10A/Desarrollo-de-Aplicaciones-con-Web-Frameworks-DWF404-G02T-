// assets/app.js
const API_BASE_URL = 'http://localhost:8080/api';

// ========== FUNCIONES DE AUTENTICACIÓN ==========
function checkAuth() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    const token = localStorage.getItem('token');
    const publicPages = ['login.html', 'register.html'];
    const currentPage = window.location.pathname.split('/').pop();
    if (!user || !token) {
        if (!publicPages.includes(currentPage)) {
            window.location.href = 'login.html';
            return false;
        }
    } else {
        if (publicPages.includes(currentPage)) {
            window.location.href = 'index.html';
            return false;
        }
    }
    return true;
}

async function login(email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Error en el login');
        }
        const data = await response.json();
        localStorage.setItem('user', JSON.stringify(data));
        localStorage.setItem('token', data.token);
        return data;
    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
}

async function register(email, password) {
    try {
        const baseUsername = email.split('@')[0];
        const timestamp = Date.now().toString().slice(-4);
        const username = baseUsername + timestamp;
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password, username })
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Error en el registro');
        }
        const data = await response.json();
        localStorage.setItem('user', JSON.stringify(data));
        localStorage.setItem('token', data.token);
        return data;
    } catch (error) {
        console.error('Register error:', error);
        throw error;
    }
}

// ✅ CORREGIDO: Evita "body stream already read"
async function apiRequest(endpoint, options = {}) {
    const token = localStorage.getItem('token');
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    };
    const config = { ...defaultOptions, ...options };
    if (config.body && typeof config.body === 'object') {
        config.body = JSON.stringify(config.body);
    }
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        if (!response.ok) {
            if (response.status === 401) {
                logout();
                throw new Error('Sesión expirada');
            }

            // ✅ Leer el cuerpo SOLO UNA VEZ
            const responseBody = await response.text();
            let errorMsg = `Error ${response.status}`;
            try {
                const errorJson = JSON.parse(responseBody);
                errorMsg = errorJson.error || errorMsg;
            } catch (e) {
                errorMsg = responseBody || errorMsg;
            }
            throw new Error(errorMsg);
        }
        return await response.json();
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
}

// ========== FUNCIONES DE DATOS ==========
async function loadCategoriesFromAPI() {
    try {
        const categories = await apiRequest('/categories');
        return categories;
    } catch (error) {
        console.error('Error loading categories:', error);
        return [];
    }
}

async function loadCoursesFromAPI() {
    try {
        const courses = await apiRequest('/courses');
        return courses;
    } catch (error) {
        console.error('Error loading courses:', error);
        return [];
    }
}

// ========== FUNCIONES DE UI ==========
async function initPage() {
    if (!checkAuth()) return;
    showUserInfo();
    const currentPage = window.location.pathname.split('/').pop();
    switch (currentPage) {
        case 'index.html':
        case '':
            await initIndexPage();
            break;
        case 'edit-course.html':
            await initEditCoursePage();
            break;
        case 'enrollment.html':
            await initEnrollmentPage();
            break;
        case 'contacts.html':
            // Puedes implementar si lo necesitas
            break;
    }
}

function showUserInfo() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    const userPill = document.getElementById('userpill');
    if (userPill && user) {
        userPill.textContent = user.email + (user.isFusalmo ? " (Admin)" : "");
        userPill.style.display = 'inline-block';
        const logoutBtn = document.getElementById('logoutBtn');
        if (logoutBtn) {
            logoutBtn.style.display = 'inline-block';
        }
    }
    const adminElements = document.querySelectorAll('.admin-only');
    adminElements.forEach(el => {
        if (user && user.isFusalmo) {
            el.classList.remove('hidden');
        } else {
            el.classList.add('hidden');
        }
    });
}

// ========== FUNCIÓN DE EDICIÓN ==========
async function initEditCoursePage() {
    const urlParams = new URLSearchParams(window.location.search);
    const courseId = urlParams.get('id');

    if (!courseId) {
        alert('ID de curso no válido');
        window.location.href = 'index.html';
        return;
    }

    try {
        const categories = await loadCategoriesFromAPI();
        const categorySelect = document.getElementById('cCategory');
        if (categorySelect) {
            categorySelect.innerHTML = '<option value="">Selecciona una categoría</option>';
            categories.forEach(cat => {
                const opt = document.createElement('option');
                opt.value = cat.id;
                opt.textContent = cat.name;
                categorySelect.appendChild(opt);
            });
        }

        const course = await apiRequest(`/courses/${courseId}`);
        if (document.getElementById('cTitle')) document.getElementById('cTitle').value = course.title || '';
        if (document.getElementById('cImage')) document.getElementById('cImage').value = course.image || 'assets/default-course.jpg';
        if (document.getElementById('cShort')) document.getElementById('cShort').value = course.shortDescription || '';
        if (document.getElementById('cInfo')) document.getElementById('cInfo').value = course.info || '';
        if (document.getElementById('cCap')) document.getElementById('cCap').value = course.capacity || 20;
        if (course.category?.id && categorySelect) {
            categorySelect.value = course.category.id;
        }

        const form = document.querySelector('#editCourseForm form');
        if (form) {
            form.onsubmit = async (e) => {
                e.preventDefault();
                const updatedData = {
                    title: document.getElementById('cTitle').value,
                    image: document.getElementById('cImage').value,
                    shortDescription: document.getElementById('cShort').value,
                    info: document.getElementById('cInfo').value,
                    capacity: parseInt(document.getElementById('cCap').value) || 20,
                    category: { id: parseInt(document.getElementById('cCategory').value) }
                };

                try {
                    await apiRequest(`/courses/${courseId}`, { method: 'PUT', body: updatedData });
                    alert('✅ Curso actualizado exitosamente');
                    window.location.href = 'index.html';
                } catch (error) {
                    alert('❌ Error al actualizar: ' + error.message);
                }
            };
        }

        const cancelBtn = document.getElementById('cancelEditBtn');
        if (cancelBtn) {
            cancelBtn.onclick = () => {
                window.location.href = 'index.html';
            };
        }

        const deleteBtn = document.getElementById('deleteCourseBtn');
        if (deleteBtn) {
            deleteBtn.onclick = async () => {
                if (confirm('¿Estás seguro de que deseas eliminar este curso?')) {
                    try {
                        await apiRequest(`/courses/${courseId}`, { method: 'DELETE' });
                        alert('✅ Curso eliminado');
                        window.location.href = 'index.html';
                    } catch (error) {
                        alert('❌ Error al eliminar: ' + error.message);
                    }
                }
            };
        }

    } catch (error) {
        console.error('Error en edición:', error);
        alert('Error al cargar el curso: ' + error.message);
        window.location.href = 'index.html';
    }
}

// ========== FUNCIÓN PARA CARGAR CATEGORÍAS EN SELECT ==========
async function loadCategoriesForSelector() {
    const categorySelect = document.getElementById('cCategory');
    if (!categorySelect) return;
    try {
        const categories = await loadCategoriesFromAPI();
        categorySelect.innerHTML = '<option value="">Selecciona una categoría</option>';
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            categorySelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error cargando categorías:', error);
        categorySelect.innerHTML = '<option value="">Error cargando categorías</option>';
    }
}

// ========== PÁGINA DE INICIO ==========
async function initIndexPage() {
    const catsContainer = document.getElementById('cats');
    const courseGrid = document.getElementById('courseGrid');
    if (!catsContainer || !courseGrid) return;

    try {
        const categories = await loadCategoriesFromAPI();
        catsContainer.innerHTML = `
            <a href="javascript:void(0)" class="badge active" onclick="filterByCategory('all')">Todas</a>
            ${categories.map(cat =>
            `<a href="javascript:void(0)" class="badge" onclick="filterByCategory('${cat.id}')">${cat.name}</a>`
        ).join('')}
        `;

        const courses = await loadCoursesFromAPI();
        if (courses.length === 0) {
            courseGrid.innerHTML = '<div class="card"><h3>No hay cursos</h3></div>';
            return;
        }

        courseGrid.innerHTML = courses.map(course => `
            <div class="card course-card" data-category="${course.category?.id || ''}">
                <img src="${course.image || 'assets/default-course.jpg'}" alt="${course.title}" style="width:100%;height:200px;object-fit:cover;">
                <div class="card-content">
                    <h3>${course.title}</h3>
                    <p class="small">${course.shortDescription || 'Sin descripción'}</p>
                    <div class="course-meta">
                        <span class="badge">${course.category?.name || 'Sin categoría'}</span>
                        <span class="badge ${course.enrolled >= course.capacity ? 'danger' : ''}">
                            Cupo: ${course.enrolled}/${course.capacity}
                        </span>
                    </div>
                    <div style="display: flex; gap: 10px; margin-top: 15px;">
                        <a href="enrollment.html?id=${course.id}" class="btn primary ${course.enrolled >= course.capacity ? 'disabled' : ''}">
                            ${course.enrolled >= course.capacity ? 'Cupo Lleno' : 'Inscribirse'}
                        </a>
                        ${isAdmin() ? `<a href="edit-course.html?id=${course.id}" class="btn small">Editar</a>` : ''}
                    </div>
                </div>
            </div>
        `).join('');

        // Configurar formulario de agregar curso
        setupAddCourseForm();
    } catch (error) {
        console.error('Error cargando página principal:', error);
    }
}

function filterByCategory(categoryId) {
    document.querySelectorAll('#cats .badge').forEach(b => b.classList.remove('active'));
    event.target.classList.add('active');
    document.querySelectorAll('.course-card').forEach(card => {
        card.style.display = (categoryId === 'all' || card.dataset.category === categoryId) ? 'block' : 'none';
    });
}

function isAdmin() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    return user && user.isFusalmo;
}

function logout() {
    if (confirm('¿Cerrar sesión?')) {
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        window.location.href = 'login.html';
    }
}

// ========== FORMULARIO DE AGREGAR CURSO ==========
function setupAddCourseForm() {
    const addCourseBtn = document.getElementById('addCourseBtn');
    const addCourseForm = document.getElementById('addCourseForm');
    const cancelAddBtn = document.getElementById('cancelAddBtn');

    if (!addCourseBtn || !addCourseForm) return;

    addCourseBtn.onclick = () => {
        addCourseForm.style.display = addCourseForm.style.display === 'none' ? 'block' : 'none';
        if (addCourseForm.style.display === 'block') {
            loadCategoriesForSelector();
        }
    };

    if (cancelAddBtn) {
        cancelAddBtn.onclick = () => {
            addCourseForm.style.display = 'none';
            addCourseForm.querySelector('form').reset();
        };
    }

    const form = addCourseForm.querySelector('form');
    if (form) {
        form.onsubmit = async (e) => {
            e.preventDefault();
            const title = document.getElementById('cTitle').value.trim();
            const categoryId = document.getElementById('cCategory').value;
            const shortDescription = document.getElementById('cShort').value.trim();
            const info = document.getElementById('cInfo').value.trim();
            const capacity = parseInt(document.getElementById('cCap').value) || 20;

            if (!title) {
                alert('El título es requerido');
                return;
            }
            if (!categoryId) {
                alert('Selecciona una categoría');
                return;
            }

            const courseData = {
                title: title,
                category: { id: parseInt(categoryId) },
                image: "assets/default-course.jpg",
                shortDescription: shortDescription,
                info: info,
                capacity: capacity,
                enrolled: 0
            };

            try {
                await apiRequest('/courses', { method: 'POST', body: courseData });
                alert('✅ Curso creado exitosamente');
                form.reset();
                addCourseForm.style.display = 'none';
                await initIndexPage(); // Recargar cursos
            } catch (error) {
                alert('❌ Error al crear curso: ' + error.message);
            }
        };
    }
}

// ========== PÁGINA DE INSCRIPCIÓN ==========
async function initEnrollmentPage() {
    const urlParams = new URLSearchParams(window.location.search);
    const courseId = urlParams.get('id');
    if (!courseId) {
        alert('Curso no especificado');
        window.location.href = 'index.html';
        return;
    }

    try {
        const course = await apiRequest(`/courses/${courseId}`);
        document.getElementById('courseInfo').innerHTML = `
            <h2>${course.title}</h2>
            <p><strong>Categoría:</strong> ${course.category?.name || 'N/A'}</p>
            <p><strong>Cupo:</strong> ${course.enrolled}/${course.capacity}</p>
        `;

        document.getElementById('enrollmentForm').onsubmit = async (e) => {
            e.preventDefault();
            const enrollmentData = {
                courseId: parseInt(courseId),
                studentName: document.getElementById('studentName').value,
                studentLastName: document.getElementById('studentLastName').value,
                studentAge: parseInt(document.getElementById('studentAge').value),
                parentName: document.getElementById('parentName').value,
                parentDui: document.getElementById('parentDui').value,
                email: document.getElementById('email').value,
                phone: document.getElementById('phone').value,
                message: document.getElementById('message').value,
                branch: document.getElementById('branch').value // ✅ Sede
            };

            if (!enrollmentData.branch) {
                alert('Por favor selecciona una sede');
                return;
            }

            try {
                await apiRequest('/enrollments', { method: 'POST', body: enrollmentData });
                alert('✅ ¡Inscripción exitosa!');
                window.location.href = 'index.html';
            } catch (error) {
                alert('❌ Error: ' + error.message);
            }
        };
    } catch (error) {
        console.error('Error cargando curso:', error);
        alert('Error al cargar el curso: ' + error.message);
        window.location.href = 'index.html';
    }
}

// ========== INICIALIZAR ==========
document.addEventListener('DOMContentLoaded', initPage);