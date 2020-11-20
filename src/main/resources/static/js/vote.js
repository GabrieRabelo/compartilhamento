function doVote(event, target, id){

    const _csrf = document.getElementsByTagName("meta").namedItem('_csrf').getAttribute('content');
    const _csrf_header = document.getElementsByTagName("meta").namedItem('_csrf_header').getAttribute('content');

    fetch(`/${event}/${target}/${id}`,{
        headers: {
            [_csrf_header]: _csrf
        },
        method: 'PUT'
    })
        .then(response => response.json().then(json => updateFrontend(json)))
        .catch(err => console.log(err))
}

function updateFrontend(data){
    let element = document.querySelector(`#${data.target}-${data.id}-score`);
    const current = parseInt(element.innerHTML);
    element.innerHTML = ''
    element.innerHTML = (current + data.delta);
}