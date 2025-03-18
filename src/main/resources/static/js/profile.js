const search = document.getElementById("searchInput");
const userList = document.getElementById("userList");

search.addEventListener("keyup", async function () {
    let query = this.value.trim();
    if (query.length === 0) {
        userList.innerHTML = '';
        return;
    }

    try {
        const response = await fetch(`/admin/search?query=${encodeURIComponent(query)}`, {method: "GET"});
        if (!response.ok) throw new Error("Failed request");

        const data = await response.json();
        userList.innerHTML = '';

        if (data && data.length > 0) {
            data.forEach(dto => {
                const userItem = document.createElement("li");
                userItem.classList.add("list-group-item", "border", "rounded", "mb-2", "p-3", "shadow-sm");
                userItem.style.cursor = "pointer";

                userItem.innerHTML = `
                        <div class="user-select-none d-flex flex-column">
                            <h5 class="fw-bold text-primary">${dto.fullName}</h5>
                            <p class="mb-1"><strong>National Code:</strong> ${dto.nationalCode}</p>
                            <p class="mb-1"><strong>Status:</strong> <span class="badge bg-info">${dto.status}</span></p>
                            <p class="mb-0"><strong>Role:</strong> <span class="badge bg-success">${dto.roleName}</span></p>
                        </div>
                    `;

                userList.appendChild(userItem);

                userItem.addEventListener("click", function () {
                    fetch(`/admin/profile?nationalCode=${encodeURIComponent(dto.nationalCode)}`, {
                        method: "GET",
                        headers: {"Content-Type": "application/json"}
                    })
                        .then(response => {
                            if (!response.ok) throw new Error("Failed to fetch profile");
                            return response.json();
                        })
                        .then(profile => {
                            showProfile(profile);
                        })
                        .catch(error => console.error("Error fetching profile:", error));
                });
            });
        } else {
            userList.innerHTML = '<li class="list-group-item text-danger text-center">No users found</li>';
        }

    } catch (error) {
        console.error("Error fetching search results:", error);
        userList.innerHTML = '<li class="list-group-item text-danger text-center">Error fetching search results</li>';
    }
});

function showProfile(profile) {
    let profileContainer = document.getElementById("profileContainer");
    profileContainer.innerHTML = `
            <div class="card shadow-lg p-4">
                <h2 class="text-center mb-4">User Profile</h2>
                <div class="text-center mb-3">
                    <img src="data:image/jpeg;base64,${profile.image}" class="profile-image img-fluid" alt="Profile Image"/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Full Name</label>
                    <p class="form-control">${profile.fullName}</p>
                </div>

                <div class="mb-3">
                    <label class="form-label">National Code</label>
                    <p class="form-control">${profile.nationalCode}</p>
                </div>

                <div class="mb-3">
                    <label class="form-label">State</label>
                    <p class="form-control">${profile.state}</p>
                </div>

                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <p class="form-control">${profile.email}</p>
                </div>

                <div class="text-center">
                    <button class="btn btn-danger w-50" onclick="hideProfile()">Close</button>
                </div>
            </div>
        `;
}

function hideProfile() {
    document.getElementById("profileContainer").innerHTML = "";
}