// Filter section
const toggleBtn = document.getElementById('toggle-filters');
const section = document.getElementById('filter-section');

toggleBtn.addEventListener('click', function() {
    if(section.style.display === 'none')
        section.style.display = 'flex';
    else
        section.style.display = 'none';
});

const orderByNameBtn = document.getElementById('order-by-name');
const orderByCreditsBtn = document.getElementById('order-by-credits');
const orderByIdBtn = document.getElementById('order-by-id');

function findAndReplaceOb(search){
    if(search.contains("ob=")){

    }
}

function addOrUpdateParam(url, param, value) {
    var regex = new RegExp("([?&])" + param + "=.*?(&|$)", "i");
    var separator = url.indexOf('?') !== -1 ? "&" : "?";
    if (url.match(regex)) {
        return url.replace(regex, '$1' + param + "=" + value + '$2');
    } else {
        return url + separator + param + "=" + value;
    }
}

// Order by section
//TODO unificar todas las function() en una

orderByNameBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"ob","subname");
});

orderByCreditsBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"ob","credits");
});


orderByIdBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"ob","id");
});


const minCreditFilterBtn = document.getElementById('min-credit-filter');
const medCreditFilterBtn = document.getElementById('med-credit-filter');
const maxCreditFilterBtn = document.getElementById('max-credit-filter');


minCreditFilterBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"credits","3");
});

medCreditFilterBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"credits","6");
});

maxCreditFilterBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"credits","9");
});

