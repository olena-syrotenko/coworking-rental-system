const menu_btn = document.querySelector("#menu-btn");
const sidebar = document.querySelector("#sidebar");
const container = document.querySelector(".my-container");

menu_btn.addEventListener("click", () => {
    sidebar.classList.toggle("active-nav");
    container.classList.toggle("active-cont");
});

const timeFormatter = (unit) => {
    return unit < 10 ? ( '0' + unit ) : unit;
}

const dateFormatter = (date) => {
    const monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    const hour = timeFormatter(date.getHours());
    const minute = timeFormatter(date.getMinutes());
    const second = timeFormatter(date.getSeconds());
    return `${date.getDate()}/${monthNames[date.getMonth()]}/${date.getFullYear()} ${hour}:${minute}:${second}`;
}

const dates = document.querySelectorAll('.dateToFormat');
for (let i = 0; i < dates.length; ++i) {
    const newDate = new Date(dates[i].innerText);
    dates[i].innerText = dateFormatter(newDate);
}