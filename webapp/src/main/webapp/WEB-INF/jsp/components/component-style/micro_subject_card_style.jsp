<style>
    .micro-subject-card {
        min-width: 100%;
        transition: transform 0.2s ease-in-out;
        -webkit-font-smoothing: subpixel-antialiased;
        backface-visibility: hidden;
    }

    .micro-subject-card:hover {
        transform: scale(1.03);
    }

    .micro-subject-card::part(base){
        min-height: 100%;
    }

    .micro-subject-card::part(body) {
        flex: 1 0 auto;
    }

    .card-title{
        font-size: 1rem;
        margin: 0;
    }
</style>