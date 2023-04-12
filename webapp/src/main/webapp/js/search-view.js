const toggleBtn = document.getElementById('toggle-filters');
const orderByNameBtn = document.getElementById('order-by-name');
const orderByCreditsBtn = document.getElementById('order-by-credits');
const orderByIdBtn = document.getElementById('order-by-id');

const section = document.getElementById('filter-section');

toggleBtn.addEventListener('click', function() {
    if(section.style.display === 'none')
        section.style.display = 'flex';
    else
        section.style.display = 'none';
});

//TODO unificar todas las function() en una
orderByNameBtn.addEventListener('click', function() {
    window.location.href = `${window.location.origin}${window.location.pathname}?ob=name`;
});

orderByCreditsBtn.addEventListener('click', function() {
    window.location.href = `${window.location.origin}${window.location.pathname}?ob=credits`;
});

orderByIdBtn.addEventListener('click', function() {
    window.location.href = `${window.location.origin}${window.location.pathname}?ob=id`;
});