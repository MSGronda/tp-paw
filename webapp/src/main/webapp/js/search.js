const form = document.querySelector('#input-form');
const input = document.querySelector('#input-text');

form.addEventListener('submit', function (event){
    event.preventDefault();
    const searchQuery = input.value.trim();
    if (searchQuery.length > 0) {
        window.location.href = `${window.location.origin}/search/${encodeURIComponent(searchQuery)}`;
    }
});