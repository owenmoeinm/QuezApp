document.addEventListener("DOMContentLoaded", function () {
        const student = document.getElementById("students");

        let students = localStorage.getItem("students");
        if (students) {
            students = JSON.parse(students);
            students.innerHTML = "";
            for (let i = 0; i < students.length; i++) {
                student.innerHTML += `<tr class="selected_student" style="cursor: pointer;"
                    onmouseover="this.style.backgroundColor='#f0f0f0';" 
                    onmouseout="this.style.backgroundColor='';">
                <td>${i + 1}</td>
                <td>
                    <img src="data:image/jpeg;base64,${students[i].image}" alt="Student Image" class="rounded-circle" width="50" height="50">
                </td>
                <td>${students[i].fullName}</td>
                <td>${students[i].email}</td>
                <td>${students[i].nationalCode}</td>
                <td class="badge ${student.status === 'CONFIRM' ? 'bg-success' : 'bg-danger'}">${students[i].status}</td></tr>`
            }
        }
    }
)
;