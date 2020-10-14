document.querySelector('#comment-form').addEventListener('submit', function (e){
    e.preventDefault()
})

function updateModal(creatorInfo) {

    const modal = document.querySelector(".modal");
    const header = document.querySelector("header");
    const mainDiv = document.querySelector("#contentDiv");
    const form = document.querySelector('#comment-form');
    const formText = document.querySelector('#text-input');

    const split = creatorInfo.split(';');
    if(split[0] === 'edit'){
        form.action = `/comment/edit/${split[1]}`;
        formText.value = document.querySelector(`#text-${split[1]}`).innerHTML;
    }else{
        form.action = `/comment/create/${split[0]}/${split[1]}`;
    }

    if(modal.style.opacity === '1') {
        modal.style.opacity = '0';
        modal.style.zIndex = '-99999';
        mainDiv.style.zIndex = '';
        header.style.zIndex = '';
    }
    else{
        modal.style.opacity = '1';
        modal.style.zIndex = '99999';
        modal.style.pointerEvents = 'auto';
        mainDiv.style.zIndex = '-1';
        header.style.zIndex = '-1';
    }
}

function closeModal() {
    const modal = document.querySelector(".modal");
    const header = document.querySelector("header");
    const mainDiv = document.querySelector("#contentDiv");
    const form = document.querySelector('#comment-form');
    const formText = document.querySelector('#text-input');

    form.action = '';
    formText.value = '';
    modal.style.opacity = '0';
    modal.style.zIndex = '-99999';
    modal.style.pointerEvents = 'none';
    mainDiv.style.zIndex = '';
    header.style.zIndex = '';
}

function submitCommentForm(){
    document.querySelector('#comment-form').submit();
}