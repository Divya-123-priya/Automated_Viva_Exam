
const API_BASE_URL = "/api";


function getToken()  { return localStorage.getItem('token'); }
function getUser()   { return JSON.parse(localStorage.getItem('user') || 'null'); }
function isAdmin()   { return getUser()?.role === 'ADMIN'; }
function isStudent() { return getUser()?.role === 'STUDENT'; }

function saveSession(data) {
  localStorage.setItem('token', data.token);
  localStorage.setItem('user',  JSON.stringify(data));
}

function clearSession() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
}


function requireLogin(redirectTo = 'login.html') {
  if (!getToken()) {
    window.location.href = redirectTo;
    return false;
  }
  return true;
}

function requireAdmin() {
  requireLogin();
  if (!isAdmin()) {
    window.location.href = 'exam.html';
    return false;
  }
  return true;
}

function requireStudent() {
  requireLogin();
  if (!isStudent()) {
    window.location.href = 'admin.html';
    return false;
  }
  return true;
}


async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
    ...(options.headers || {})
  };

  const res = await fetch(API_BASE_URL + path, { ...options, headers });

  if (res.status === 401) {
    clearSession();
    window.location.href = 'login.html';
    throw new Error('Session expired. Please log in again.');
  }

 
  const contentType = res.headers.get('content-type') || '';
  const body = contentType.includes('application/json')
    ? await res.json()
    : await res.text();

  if (!res.ok) {
    const msg = (typeof body === 'object' ? body.error : body) || `Error ${res.status}`;
    throw new Error(msg);
  }

  return body;
}


async function apiLogin(email, password) {
  const data = await apiFetch('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password })
  });
  saveSession(data);
  return data;
}

async function apiRegister(email, password, role) {
  return apiFetch('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ email, password, role })
  });
}

function apiLogout() {
  clearSession();
  window.location.href = 'login.html';
}


async function apiGetSubjects() {
  return apiFetch('/admin/subjects');
}

async function apiAddSubject(subjectName, description) {
  return apiFetch('/admin/subject', {
    method: 'POST',
    body: JSON.stringify({ subjectName, description })
  });
}


async function apiUploadMaterial(file, subjectId) {
  const token = getToken();
  const fd = new FormData();
  fd.append('file', file);
  fd.append('subjectId', subjectId);

  const res = await fetch(`${API_BASE_URL}/admin/uploadMaterial`, {
    method: 'POST',
    headers: { 'Authorization': `Bearer ${token}` },
    body: fd   
  });

  const text = await res.text();
  if (!res.ok) throw new Error(text || 'Upload failed');
  return text;
}

async function apiGetMaterials(subjectId) {
  return apiFetch(`/admin/materials/${subjectId}`);
}


async function apiStartSession(studentId, subject, totalQuestions) {
  return apiFetch('/session/start', {
    method: 'POST',
    body: JSON.stringify({ studentId, subject, totalQuestions })
  });
}

async function apiCompleteSession(sessionId) {
  return apiFetch(`/session/${sessionId}/complete`, { method: 'PUT' });
}


async function apiGenerateQuestion(subject, difficulty, previousQuestions = []) {
  const prev = previousQuestions.length > 0
    ? '&previousQuestions=' + encodeURIComponent(previousQuestions.join('||'))
    : '';
  return apiFetch(`/viva/question?subject=${encodeURIComponent(subject)}&difficulty=${difficulty}${prev}`);
}

async function apiGenerateFromMaterial(materialId, difficulty) {
  return apiFetch(`/viva/questionFromMaterial?materialId=${materialId}&difficulty=${difficulty}`);
}


async function apiSubmitAnswer(questionId, sessionId, answerText) {
  await apiFetch('/viva/answer', {
    method: 'POST',
    body: JSON.stringify({ questionId, sessionId, answerText })
  });
}


async function apiGetResults(page = 0, size = 20) {
  return apiFetch(`/admin/results?page=${page}&size=${size}`);
}

async function apiGetStudentResults(email) {
  return apiFetch(`/admin/results/student/${encodeURIComponent(email)}`);
}

async function apiResendEmail(resultId) {
  return apiFetch(`/admin/results/${resultId}/email`, { method: 'POST' });
}


async function apiGetSummary() {
  return apiFetch('/admin/analytics/summary');
}

async function apiGetTopPerformers(limit = 5) {
  return apiFetch(`/admin/analytics/top?limit=${limit}`);
}

async function apiGetBottomPerformers(limit = 5) {
  return apiFetch(`/admin/analytics/bottom?limit=${limit}`);
}


function showAlert(id, message, type = 'danger') {
  const el = document.getElementById(id);
  if (!el) return;
  el.className = `alert alert-${type} show`;
  el.textContent = message;
}

function hideAlert(id) {
  const el = document.getElementById(id);
  if (el) el.className = 'alert';
}

function renderNavUser() {
  const user = getUser();
  if (!user) return;
  const displayName = (user.fullName && user.fullName.trim()) ? user.fullName : user.email;
  const emailEl = document.getElementById('nav-email');
  const nameEl  = document.getElementById('nav-name');
  const roleEl  = document.getElementById('nav-role');
  if (emailEl) emailEl.textContent = displayName;
  if (nameEl)  nameEl.textContent  = displayName;
  if (roleEl)  roleEl.textContent  = user.role;
}

function initTabs() {
  document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const target = btn.dataset.tab;
      document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
      document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
      btn.classList.add('active');
      document.getElementById(target)?.classList.add('active');
    });
  });
}

function scoreToPercent(score) { return Math.round(score * 10); }

function gradeClass(grade) {
  const map = { A:'badge-success', B:'badge-primary', C:'badge-warning', D:'badge-warning', F:'badge-danger' };
  return map[grade] || 'badge-primary';
}
