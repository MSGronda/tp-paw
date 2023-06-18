function modifyProgressButton(text,backgroundColor, textColor) {

    const button = document.querySelector('sl-button.progress-bt')

    button.textContent = text;


    console.log(button.shadowRoot)
    console.log(button.shadowRoot.querySelector('[part="base"]'))


    const buttonStyle = button.shadowRoot.querySelector('[part="base"]');
    buttonStyle.style.backgroundColor= backgroundColor;
    buttonStyle.style.color = textColor
}

function submitSubjectProgressForm(url, formId, newProgress, newProgresText) {

    $('#' + formId + ' input[name=progress]').val(newProgress)

    $.ajax({
        url: url,
        type: 'POST',
        data: $('#'+formId).serialize(),
        success: function(_response) {
            if(newProgress === 1){
                modifyProgressButton(newProgresText, "#0284c7", "#ffffff")
            }
            else{
                modifyProgressButton(newProgresText, "#ffffff","#0284c7" )
            }
        },
        error: function(xhr, status, error) {

        }
    });
}