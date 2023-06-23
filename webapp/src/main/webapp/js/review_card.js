function updateCounters(formId, changeLikes, changeDislikes){
    const likehtml = $('#like-number-'+formId)
    const like_string = likehtml.text()
    likehtml.text(parseInt(like_string) + changeLikes)

    const dislikehtml = $('#dislike-number-'+formId)
    const dislike_string = dislikehtml.text()
    dislikehtml.text(parseInt(dislike_string) + changeDislikes)
}

function submitReviewVoteForm(url,formId, prevVote ,newVote) {
    $('#' + formId + ' input[name=vote]').val(newVote)
    $.ajax({
        url: url,
        type: 'POST',
        data: $('#'+formId).serialize(),
        success: function(_response) {

            const like = $('#like-icon-'+formId)
            const dislike = $('#dislike-icon-'+formId)
            switch (newVote) {
                case 0:
                    like.css("color","#4a90e2")
                    dislike.css("color","#4a90e2")

                    if(prevVote === 1)
                        updateCounters(formId,-1,0);
                    else
                        updateCounters(formId,0,-1);
                    break;
                case 1:
                    like.css("color","#f5a623")
                    dislike.css("color","#4a90e2")
                    if(prevVote === 0)
                        updateCounters(formId,1,0)
                    else
                        updateCounters(formId,1,-1)
                    break;
                case -1:
                    like.css("color","#4a90e2")
                    dislike.css("color","#f5a623")
                    if(prevVote === 0)
                        updateCounters(formId,0,1)
                    else
                        updateCounters(formId,-1,1)
                    break;
            }

        },
        error: function(xhr, status, error) {

        }
    });
}

const showMoreButtons = document.querySelectorAll('.show-button');

showMoreButtons.forEach(button => {
    const showLessButtonText = button.querySelector('.show-less-text');

    if(showLessButtonText !== null) {
        showLessButtonText.style.display = 'none';
    }
})

showMoreButtons.forEach(button => button.addEventListener('click',() => {
    const dots = button.parentElement.querySelector(".dots");
    const moreText = button.parentElement.querySelector(".more-text");
    const showLessButtonText = button.querySelector('.show-less-text');
    const showMoreButtonText = button.querySelector('.show-more-text');
    if(dots !== null && moreText !== null){
        if (dots.style.display === "none") {
            dots.style.display = "inline";
            moreText.style.display = "none";

            showMoreButtonText.style.display = 'inline';
            showLessButtonText.style.display = 'none';
        } else {
            dots.style.display = "none";
            moreText.style.display = "inline";

            showMoreButtonText.style.display = 'none';
            showLessButtonText.style.display = 'inline';
        }
    }
}))