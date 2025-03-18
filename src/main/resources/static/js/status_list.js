
const fullName = document.getElementById("name").innerText
const nationalCode = document.getElementById("nationalCode").innerText
const status = document.getElementById("status").innerText
const roleName = document.getElementById("role").innerText

const convert = document.getElementById("convert")

convert.addEventListener("click", () => {

    let form = new FormData();
    form.append("fullName", fullName);
    form.append("nationalCode", nationalCode);
    form.append("status", status)
    form.append("roleName", roleName)

    fetch('/admin/convert', {
        method: 'POST',
        body: form
    })
        .then(response => response.text())
        .then(data => {
            console.log('نتیجه سرور:', data);
            convert.classList.replace("btn-danger", "btn-success");
            location.reload();
            convert.textContent = "تأیید شد";
            convert.disabled = true;
        })
        .catch(error => {
            console.error('خطا:', error);
            alert('مشکلی پیش آمد، لطفاً دوباره تلاش کنید.');
        });

})

