
// perdon


function updateCounters(formId, changeLikes, changeDislikes){
    const likehtml = $('#like-number-'+formId)
    const like_string = likehtml.text()
    likehtml.text(parseInt(like_string) + changeLikes)

    const dislikehtml = $('#dislike-number-'+formId)
    const dislike_string = dislikehtml.text()
    dislikehtml.text(parseInt(dislike_string) + changeDislikes)
}

function submitForm(url,formId, prevVote ,newVote) {

    $('#' + formId + ' input[name=vote]').val(newVote)

    $.ajax({
        url: url,
        type: 'POST',
        data: $('#'+formId).serialize(),
        success: function(response) {

            // const likeChage = 1, dislikeChange = prevVote === 0 ? 0 : -1
            var likeChange, dislikeChange


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