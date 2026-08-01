/* ============================================================
                        JOB MART
                    app.js (Module 1)
   ============================================================ */

/* ============================================================
                        CONFIGURATION
   ============================================================ */

const API_BASE = "hire-hub-production.up.railway.app";

/* ============================================================
                        API ENDPOINTS
   ============================================================ */

const API = {

    SIGNUP: "/user/signup",

    LOGIN: "/user/login",

    PROFILE: "/user/my-profile",

    UPDATE_PROFILE: "/user/update-profile",

    UPLOAD_RESUME: "/user/upload-resume",

    DOWNLOAD_RESUME: "/user/download-resume/",

    VIEW_RESUME: "/user/view-resume/",

    JOBS: "/jobs",

    CREATE_JOB: "/jobs/create",

    MY_JOBS: "/jobs/myjobs",

    APPLY_JOB: "/Applications/apply/",

    APPLICANTS: "/Applications/applicants/",

    UPDATE_STATUS: "/Applications/"

};

/* ============================================================
                        GLOBAL STATE
   ============================================================ */

const app = {

    token: localStorage.getItem("token"),

    user: null,

    page: 1,

    pageSize: 5,

    totalPages: 1,

    sortBy: "postedAt",

    direction: "desc",

    filters: {

        title: "",

        company: "",

        location: ""

    }

};

/* ============================================================
                        DOM CACHE
   ============================================================ */

const DOM = {

    loader: document.getElementById("loader"),

    toastContainer: document.getElementById("toastContainer"),

    homePage: document.getElementById("homePage"),

    jobsPage: document.getElementById("jobsPage"),

    profilePage: document.getElementById("profilePage"),

    dashboardPage: document.getElementById("dashboardPage"),

    applicantPage: document.getElementById("applicantPage"),

    loginModal: document.getElementById("loginModal"),

    signupModal: document.getElementById("signupModal"),

    createJobModal: document.getElementById("jobModal")

};

/* ============================================================
                        LOADER
   ============================================================ */

function showLoader() {

    DOM.loader.classList.remove("hidden");

}

function hideLoader() {

    DOM.loader.classList.add("hidden");

}

/* ============================================================
                        TOAST
   ============================================================ */

function showToast(message, type = "success") {

    const toast = document.createElement("div");

    toast.className = `toast ${type}`;

    toast.textContent = message;

    DOM.toastContainer.appendChild(toast);

    setTimeout(() => {

        toast.remove();

    }, 3000);

}

/* ============================================================
                        TOKEN
   ============================================================ */

function saveToken(token) {

    app.token = token;

    localStorage.setItem("token", token);

}

function clearToken() {

    app.token = null;

    app.user = null;

    localStorage.removeItem("token");

}

function isLoggedIn() {

    return app.token !== null;

}

/* ============================================================
                        HEADERS
   ============================================================ */

function authHeaders() {

    return {

        Authorization: `Bearer ${app.token}`

    };

}

function jsonHeaders() {

    return {

        "Content-Type": "application/json",

        Authorization: `Bearer ${app.token}`

    };

}

/* ============================================================
                        API REQUEST
   ============================================================ */

async function apiRequest(url, options = {}) {

    showLoader();

    try {

        const response = await fetch(API_BASE + url, options);

        const contentType = response.headers.get("content-type");

        let data = null;

        if (contentType && contentType.includes("application/json")) {

            data = await response.json();

        } else {

            data = await response.text();

        }

        if (!response.ok) {

            const message = data?.message || data || "Request Failed";

            throw new Error(message);

        }

        return data;

    }

    catch (error) {

        showToast(error.message, "error");

        throw error;

    }

    finally {

        hideLoader();

    }

}

/* ============================================================
                        UTILITIES
   ============================================================ */

function timeAgo(date){

    const diff=Math.floor(
        (Date.now()-new Date(date))/1000
    );

    const minute=60;
    const hour=3600;
    const day=86400;

    if(diff<minute)
        return "Just now";

    if(diff<hour)
        return Math.floor(diff/minute)+" minutes ago";

    if(diff<day)
        return Math.floor(diff/hour)+" hours ago";

    return Math.floor(diff/day)+" days ago";
}

function hideAllPages() {

    DOM.homePage.classList.add("hidden");

    DOM.jobsPage.classList.add("hidden");

    DOM.profilePage.classList.add("hidden");

    DOM.dashboardPage.classList.add("hidden");

    DOM.applicantPage.classList.add("hidden");

}

function showPage(page) {

    hideAllPages();

    page.classList.remove("hidden");

}

function openModal(modal) {

    modal.classList.remove("hidden");

}

function closeModal(modal) {

    modal.classList.add("hidden");

}

/* ============================================================
                    GLOBAL EVENTS
   ============================================================ */

document.getElementById("logoutBtn")
    .addEventListener("click", logout);

document.getElementById("loginBtn")
    .addEventListener("click", () => openModal(DOM.loginModal));

document.getElementById("signupBtn")
    .addEventListener("click", () => openModal(DOM.signupModal));

document.getElementById("closeLogin")
    .addEventListener("click", () => closeModal(DOM.loginModal));

document.getElementById("closeSignup")
    .addEventListener("click", () => closeModal(DOM.signupModal));

document.getElementById("closeJob")
    .addEventListener("click", () => closeModal(DOM.createJobModal));

const createJobBtn = document.getElementById("createJobBtn");

if (createJobBtn) {

    createJobBtn.addEventListener("click", () => {
        openModal(DOM.createJobModal);
    });

}

/* ============================================================
                    AUTHENTICATION MODULE
   ============================================================ */

const Auth = {

    async login(email, password) {

        try {

            const response = await apiRequest(API.LOGIN, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    email,
                    password
                })

            });

            saveToken(response.token);

            await this.loadProfile();

            updateNavbar();

            closeModal(DOM.loginModal);

            showToast("Login Successful");

            showPage(DOM.homePage);

        }

        catch (error) {

            console.error(error);

        }

    },



    async signup(name, email, password, role) {

        try {

            await apiRequest(API.SIGNUP, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    name,

                    email,

                    password,

                    role

                })

            });

            closeModal(DOM.signupModal);

            showToast("Registration Successful");

        }

        catch (error) {

            console.error(error);

        }

    },



    async loadProfile() {

        try {

            const user = await apiRequest(

                API.PROFILE,

                {

                    headers: authHeaders()

                }

            );

            app.user = user;

            updateNavbar();

            return user;

        }

        catch (error) {

            clearToken();

            updateNavbar();

        }

    },



    async autoLogin() {

        if (!isLoggedIn()) {

            return;

        }

        await this.loadProfile();

    }

};



/* ============================================================
                    NAVIGATION BAR
   ============================================================ */

function updateNavbar() {

    const loginBtn = document.getElementById("loginBtn");

    const signupBtn = document.getElementById("signupBtn");

    const logoutBtn = document.getElementById("logoutBtn");

    const dashboardNav = document.getElementById("dashboardNav");

    if (!isLoggedIn()) {

        loginBtn.classList.remove("hidden");

        signupBtn.classList.remove("hidden");

        logoutBtn.classList.add("hidden");

        dashboardNav.classList.add("hidden");

        return;

    }

    loginBtn.classList.add("hidden");

    signupBtn.classList.add("hidden");

    logoutBtn.classList.remove("hidden");

    if (app.user && app.user.roles === "RECRUITER") {

        dashboardNav.classList.remove("hidden");

    }

    else {

        dashboardNav.classList.add("hidden");

    }

}

/* ============================================================
                    LOGIN FORM
   ============================================================ */

const loginForm = document.getElementById("loginForm");

loginForm.addEventListener(

    "submit",

    async function (e) {

        e.preventDefault();

        const email = document.getElementById("loginEmail").value;

        const password = document.getElementById("loginPassword").value;

        await Auth.login(

            email,

            password

        );

    }

);

/* ============================================================
                    SIGNUP FORM
   ============================================================ */

const signupForm = document.getElementById("signupForm");

signupForm.addEventListener(

    "submit",

    async function (e) {

        e.preventDefault();

        await Auth.signup(

            document.getElementById("signupName").value,

            document.getElementById("signupEmail").value,

            document.getElementById("signupPassword").value,

            document.getElementById("signupRole").value

        );

    }

);
/* ============================================================
                    ROUTE PROTECTION
   ============================================================ */

function requireLogin(page) {

    if (!isLoggedIn()) {

        showToast(

            "Please login first",

            "error"

        );

        openModal(DOM.loginModal);

        return;

    }

    showPage(page);

}
/* ============================================================
                    STARTUP
   ============================================================ */

window.addEventListener(

    "load",

    async () => {

        updateNavbar();

        await Auth.autoLogin();

    }

);
/* ============================================================
                    PROFILE MODULE
   ============================================================ */

const Profile = {

    async load() {

        try {

            const user = await Auth.loadProfile();

            if (user) {

                this.render(user);

            }

        } catch (error) {

            console.error(error);

        }

    },

    render(user) {

        const container = document.getElementById("profileDetails");

        if (!container) return;

        container.innerHTML = `

            <div class="profile-item">
                <label>Name</label>
                <span>${user.name}</span>
            </div>

            <div class="profile-item">
                <label>Email</label>
                <span>${user.email}</span>
            </div>

            <div class="profile-item">
                <label>Role</label>
                <span>${user.roles}</span>
            </div>

            <div class="profile-item">
                <label>Resume</label>
                <span>${user.resumePath ?? "Not Uploaded"}</span>
            </div>

        `;

        document.getElementById("updateName").value = user.name;

        document.getElementById("updateEmail").value = user.email;

    },

    async update() {

        try {

            const body = {

                name: document.getElementById("updateName").value,

                email: document.getElementById("updateEmail").value

            };

            const user = await apiRequest(

                API.UPDATE_PROFILE,

                {

                    method: "PUT",

                    headers: jsonHeaders(),

                    body: JSON.stringify(body)

                }

            );

            app.user = user;

            this.render(user);

            updateNavbar();

            showToast("Profile Updated");

        }

        catch (error) {

            console.error(error);

        }

    }

};
/* ============================================================
                UPDATE PROFILE FORM
   ============================================================ */

const updateProfileForm =

document.getElementById("updateProfileForm");

if(updateProfileForm){

updateProfileForm.addEventListener(

"submit",

async function(e){

e.preventDefault();

await Profile.update();

}

);

}
/* ============================================================
                    RESUME UPLOAD
   ============================================================ */

const Resume = {

    async upload() {

        const file =

        document.getElementById("resumeFile").files[0];

        if (!file) {

            showToast(

                "Please select a resume",

                "error"

            );

            return;

        }

        const formData = new FormData();

        formData.append(

            "file",

            file

        );

        try {

            const response = await apiRequest(

                API.UPLOAD_RESUME,

                {

                    method: "POST",

                    headers: authHeaders(),

                    body: formData

                }

            );

            showToast(response.message);

            await Profile.load();

        }

        catch (error) {

            console.error(error);

        }

    },

    async viewApplicant(userId){

    const viewer = document.getElementById("resumeViewer");

    viewer.innerHTML = `

        <div class="resume-toolbar">

            <button
                onclick="Resume.downloadApplicant(${userId})">

                Download Resume

            </button>

        </div>

        <div id="pdfPages"></div>

    `;

    try{

        const loadingTask = pdfjsLib.getDocument(
            `${API_BASE}/user/view-resume/${userId}`
        );

        const pdf = await loadingTask.promise;

        const container = document.getElementById("pdfPages");

        for(let pageNumber = 1; pageNumber <= pdf.numPages; pageNumber++){

            const page = await pdf.getPage(pageNumber);

            const viewport = page.getViewport({ scale: 1 });

            const canvas = document.createElement("canvas");

            const context = canvas.getContext("2d");

            canvas.width = viewport.width;
            canvas.height = viewport.height;

            canvas.className = "pdf-page";

            container.appendChild(canvas);

            await page.render({

                canvasContext: context,

                viewport: viewport

            }).promise;
        }

    }catch(e){

        console.error(e);

        viewer.innerHTML += `<p style="color:red">Failed to load PDF.</p>`;

    }

},

    async download() {

        try {

            const response = await fetch(

                API_BASE +

                API.DOWNLOAD_RESUME +

                app.user.id,

                {

                    headers: authHeaders()

                }

            );

            if (!response.ok)

                throw new Error(

                    "Unable to download"

                );

            const blob =

            await response.blob();

            const url =

            window.URL.createObjectURL(blob);

            const a =

            document.createElement("a");

            a.href = url;

            a.download = "Resume.pdf";

            document.body.appendChild(a);

            a.click();

            a.remove();

            window.URL.revokeObjectURL(url);

        }

        catch (error) {

            showToast(

                error.message,

                "error"

            );

        }

    }

};
/* ============================================================
                    RESUME EVENTS
   ============================================================ */

const uploadBtn =

document.getElementById("uploadResume");

if(uploadBtn){

uploadBtn.addEventListener(

"click",

()=>Resume.upload()

);

}

const downloadBtn =document.getElementById("downloadResume");

if(downloadBtn){

 downloadBtn.addEventListener("click",()=>Resume.download());

}

/* ============================================================
                    JOB MODULE
============================================================ */

const Jobs = {

    jobs: [],

    selectedJob:null,

    async load(page = 1) {

        app.page = page;

        const params = new URLSearchParams({

            page,

            size: app.pageSize,

            sortBy: app.sortBy,

            direction: app.direction,

            title: app.filters.title,

            company: app.filters.company,

            location: app.filters.location

        });

        try {

            const response = await apiRequest(

                API.JOBS + "?" + params.toString(),

                {
                    headers: authHeaders()
                }

            );

            this.jobs = response.content;

            app.totalPages = response.totalPages;

            this.render();

         if(this.jobs.length > 0){
         this.selectedJob = this.jobs[0];
        }else{
           this.selectedJob = null;
        }
            this.render();
            this.renderDetails();
            this.renderPagination();

        }

        catch(error){

            console.error(error);

        }

    },
    showDetails(jobId){

    this.selectedJob = this.jobs.find(
        j => j.id === jobId
    );

    this.render();

    this.renderDetails();

},

    render(){

    const container=document.getElementById("jobsContainer");

    if(!container) return;

    container.innerHTML="";

    if(this.jobs.length===0){

    container.innerHTML=`

    <div class="empty-state">

        <h2>😔</h2>

        <h3>No Jobs Found</h3>

        <p>Try changing your search filters.</p>

    </div>

    `;

    return;

}

    this.jobs.forEach(job=>{

        container.innerHTML += `

<div
    class="job-card job-summary ${this.selectedJob?.id===job.id ? "selected-job" : ""}"
    onclick="Jobs.showDetails(${job.id})">

    <h3>${job.title}</h3>

    <p>${job.company}</p>

    <p>${job.location}</p>

    <p>₹ ${(job.salary / 100000).toFixed(0)} LPA</p>

    <p class="posted-date">
        Posted ${timeAgo(job.postedAt)}
    </p>

</div>

`;

    });

},

renderDetails(){

    const details = document.getElementById("jobDetails");
    const job = this.selectedJob;

    try{

        details.innerHTML = `
            <h2>${job.title}</h2>
            <p>${job.company}</p>
        `;

        details.innerHTML += `<p>Location : ${job.location}</p>`;

        details.innerHTML += `<p>Salary : ₹ ${(job.salary / 100000).toFixed(0)} LPA</p>`;

        details.innerHTML += `<p>Posted : ${timeAgo(job.postedAt)}</p>`;

        details.innerHTML += `<p>Description : ${job.description}</p>`;

        details.innerHTML += `<p>Recruiter : ${job.recruiterName}</p>`;

        details.innerHTML += `<p>Recruiter Email : ${job.recruiterEmail}</p>`;

        details.innerHTML += `
            <button class="primary apply-btn"
                onclick="Jobs.apply(${job.id})">
                Apply
            </button>
        `;

    }catch(e){
        alert(e);
        console.error(e);
    }
},
async apply(jobId){

        try{

            const response=

            await apiRequest(

                API.APPLY_JOB+jobId,

                {

                    method:"POST",

                    headers:authHeaders()

                }

            );

            showToast(response);

        }

        catch(error){

            console.error(error);

        }

    },
        renderPagination(){

        const container=

        document.getElementById("pagination");

        if(!container) return;

        container.innerHTML="";

        for(let i=1;i<=app.totalPages;i++){

            container.innerHTML+=`

<button

class="${i===app.page ? 'active':''}" 

onclick="Jobs.load(${i})">${i}

</button>

`;

        }

    }

};
document.getElementById("searchBtn")

.addEventListener(

"click",

()=>{

app.filters.title=

document.getElementById("searchTitle").value;

app.filters.company=

document.getElementById("searchCompany").value;

app.filters.location=

document.getElementById("searchLocation").value;

Jobs.load();

}

);
document.getElementById("sortBy")

.addEventListener(

"change",

e=>{

app.sortBy=e.target.value;

Jobs.load();

}

);

document.getElementById("direction")

.addEventListener(

"change",

e=>{

app.direction=e.target.value;

Jobs.load();

}

);

/* ============================================================
                RECRUITER MODULE
============================================================ */

const Recruiter = {

    myJobs: [],

    applicants: [],

    async createJob(job) {

        console.log("Inside createJob()");
        console.log(job);

        try {

            console.log(API.CREATE_JOB);

            await apiRequest(

                API.CREATE_JOB,

                {

                    method: "POST",

                    headers: jsonHeaders(),

                    body: JSON.stringify(job)

                }

            );

            closeModal(DOM.createJobModal);

            showToast("Job Posted Successfully");

            this.loadMyJobs();

        }

        catch(error){

            console.error(error);

        }

    },
        async loadMyJobs(){

        try{

            this.myJobs = await apiRequest(

                API.MY_JOBS,

                {

                    headers: authHeaders()

                }

            );

            this.renderMyJobs();

        }

        catch(error){

            console.error(error);

        }

    },
        renderMyJobs(){

        const container =

        document.getElementById("myJobsContainer");

        if(!container) return;

        container.innerHTML="";

        if(this.myJobs.length===0){

            container.innerHTML="<h3>No Jobs Posted</h3>";

            return;

        }

        this.myJobs.forEach(job=>{

            container.innerHTML += `

<div class="job-card">

    <h2>${job.title}</h2>

    <hr>

    <p><strong>Company:</strong> ${job.company}</p>

    <p><strong>Location:</strong> ${job.location}</p>

    <p><strong>Salary:</strong> ₹ ${(job.salary / 100000).toFixed(0)} LPA</p>

    <p><strong>Description:</strong> ${job.description}</p>

    <p><strong>Posted:</strong> ${timeAgo(job.postedAt)}</p>

<button onclick="Recruiter.loadApplicants(${job.id})">

Applicants

</button>

</div>

`;

        });

    },
        async loadApplicants(jobId){

        try{

            this.applicants =

            await apiRequest(

                API.APPLICANTS + jobId,

                {

                    headers: authHeaders()

                }

            );

            this.renderApplicants(jobId);

            showPage(DOM.applicantPage);

        }

        catch(error){

            console.error(error);

        }

    },
        renderApplicants(jobId){

        const container =

        document.getElementById("applicantsContainer");

        container.innerHTML="";

        if(this.applicants.length===0){

            container.innerHTML="<h3>No Applicants Yet</h3>";

            return;

        }

        this.applicants.forEach(app=>{

container.innerHTML += `

<div class="applicant-card">

<h3>${app.candidateName}</h3>

<p>${app.candidateEmail}</p>

<p>Status : ${app.applicationStatus}</p>

<button onclick="Resume.viewApplicant(${app.candidateId})">

View Resume

</button>

<select
onchange="Recruiter.updateStatus(
${app.applicationId},
this.value,
${jobId}
)">

<option value="">Update</option>

<option value="ACCEPTED">

Accept

</option>

<option value="REJECTED">

Reject

</option>


</select>

</div>

`;

        });

    },

        async updateStatus(applicationId,status,jobId){

        if(status==="") return;

        try{

            await apiRequest(

                API.UPDATE_STATUS +

                applicationId +

                "/status",

                {

                    method:"PUT",

                    headers:jsonHeaders(),

                    body:JSON.stringify({

                        status

                    })

                }

            );

            showToast(

                "Status Updated"

            );

            this.loadApplicants(jobId);

        }

        catch(error){

            console.error(error);

        }

    }

};

const createJobForm =

document.getElementById("createJobForm");

if(createJobForm){

createJobForm.addEventListener(

"submit",

async function(e){

e.preventDefault();

console.log("Submit event fired");

await Recruiter.createJob({

title:
document.getElementById("jobTitle").value,

description:
document.getElementById("jobDescription").value,

company:
document.getElementById("jobCompany").value,

location:
document.getElementById("jobLocation").value,

salary: Number(document.getElementById("jobSalary").value) * 100000

});

}

);

}


/* ============================================================
                    APPLICATION MODULE
============================================================ */

const App = {

    async initialize() {

        try {

            updateNavbar();

            if (isLoggedIn()) {

                await Auth.loadProfile();

                updateNavbar();

            }

            this.registerNavigation();

            showPage(DOM.homePage);

            showToast("Welcome to Job Mart");

        } catch (error) {

            console.error(error);

        }

    },

    registerNavigation() {

        const homeNav = document.getElementById("homeNav");

        if (homeNav) {

            homeNav.addEventListener("click", () => {

                showPage(DOM.homePage);

            });

        }

        const jobsNav = document.getElementById("jobsNav");

        if (jobsNav) {

            jobsNav.addEventListener("click", async () => {

                requireLogin(DOM.jobsPage);

                await Jobs.load();

            });

        }

        const profileNav = document.getElementById("profileNav");

        if (profileNav) {

            profileNav.addEventListener("click", async () => {

                requireLogin(DOM.profilePage);

                await Profile.load();

            });

        }

        const dashboardNav = document.getElementById("dashboardNav");

        if (dashboardNav) {

            dashboardNav.addEventListener("click", async () => {

                if (app.user?.roles !== "RECRUITER") {

                    showToast("Recruiter access only", "error");

                    return;

                }

                showPage(DOM.dashboardPage);

                await Recruiter.loadMyJobs();

            });

        }

    }

};
/* ============================================================
                    LIVE SEARCH
============================================================ */

const titleSearch = document.getElementById("searchTitle");

if (titleSearch) {

    titleSearch.addEventListener("keyup", () => {

        app.filters.title = titleSearch.value;

        Jobs.load(1);

    });

}
document.addEventListener("resumeUploaded", async () => {

    await Profile.load();

});
window.addEventListener("error", function (e) {

    console.error(e.error);

});
document.querySelectorAll(".modal").forEach(modal => {

    modal.addEventListener("click", function (e) {

        if (e.target === modal) {

            modal.classList.add("hidden");

        }

    });

});
document.addEventListener("keydown", function (e) {

    if (e.key === "Escape") {

        document.querySelectorAll(".modal")

            .forEach(modal => {

                modal.classList.add("hidden");

            });

    }

});
function buttonLoading(button, loading) {

    if (!button) return;

    if (loading) {

        button.disabled = true;

        button.dataset.originalText = button.innerText;

        button.innerText = "Loading...";

    } else {

        button.disabled = false;

        button.innerText = button.dataset.originalText || "Submit";

    }

}
function logout() {

    clearToken();

    app.user = null;

    updateNavbar();

    showPage(DOM.homePage);

    showToast("Logged Out Successfully");

}
window.addEventListener("DOMContentLoaded", async () => {

    hideLoader();          // Hide loader immediately

    await App.initialize();

});