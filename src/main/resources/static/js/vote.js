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
    let element = document.querySelector(`#${data.target}-${data.id}-score`);
    const current = parseInt(element.innerHTML);
    element.innerHTML = ''
    element.innerHTML = (current + data.delta);
    showCustomToast('success', 'Voto registrado com sucesso!');
}