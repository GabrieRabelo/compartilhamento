/**
 * Performs a fetch PUT to execute a vote action
 * @param event upvote or downvote
 * @param target question or answer
 * @param id target id
 */
function doVote(event, target, id){

    const _csrf = document.getElementsByTagName("meta").namedItem('_csrf').getAttribute('content');
    const _csrf_header = document.getElementsByTagName("meta").namedItem('_csrf_header').getAttribute('content');

    fetch(`/${event}/${target}/${id}`,{
        headers: { [_csrf_header]: _csrf },
        method: 'PUT'}).then(response => {
            if(response.ok) response.json().then(json => updateFrontend(json));
            else showCustomToast('error', 'Não foi possível realizar essa ação.')
        })
        .catch(err => console.log(err))
}

/**
 * Updates the frontend if doVote was successful
 * @param data Response JSON
 */
function updateFrontend(data){
    let scoreElement = document.querySelector(`#${data.target}-${data.id}-score`);
    let iconElements = document.querySelector(`#${data.target}-${data.id}-score-actions`);

    //[0] == upvote, [1] == downVote
    let icons = iconElements.querySelectorAll('img');

    if(data.rollback){
        icons[0].src = '/img/icons/upvote.svg';
        icons[1].src = '/img/icons/downvote.svg';
    }
    else if(data.delta > 0 && !data.rollback){
        icons[0].src = '/img/icons/upvoted.svg';
        icons[1].src = '/img/icons/downvote.svg';
    }
    else if(data.delta < 0 && !data.rollback){
        icons[0].src = '/img/icons/upvote.svg';
        icons[1].src = '/img/icons/downvoted.svg';
    }

    const current = parseInt(scoreElement.innerHTML);
    const sum = (current + data.delta);
    console.log(current)
    console.log(data.delta)
    scoreElement.innerHTML = ''
    scoreElement.innerHTML = sum;

    showCustomToast('success', 'Voto registrado com sucesso!');
}