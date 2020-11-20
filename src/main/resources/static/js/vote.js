function doVote(event, target, id){

    const _csrf = document.getElementsByTagName("meta").namedItem('_csrf').getAttribute('content');
    const _csrf_header = document.getElementsByTagName("meta").namedItem('_csrf_header').getAttribute('content');
    let xhr = new XMLHttpRequest();
    xhr.responseType = 'json';

    xhr.onreadystatechange = function (){
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                updateFrontend(xhr.response);
            } else {
                console.log("NOPE");
            }
        }
    }

    xhr.open('PUT', `/${event}/${target}/${id}`, true);
    xhr.setRequestHeader(_csrf_header, _csrf);
    xhr.send();
}

function updateFrontend(data){
    let element = document.querySelector(`#${data.target}-${data.id}-score`);
    const current = parseInt(element.innerHTML);
    element.innerHTML = ''
    element.innerHTML = (current + data.delta);
}