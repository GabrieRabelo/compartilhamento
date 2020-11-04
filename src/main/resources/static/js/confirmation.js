document.querySelector('#modal-form').addEventListener('submit', function (e){
    e.preventDefault()
})

function updateConfirmation(creatorInfo) {

    const confirmationModal = document.querySelector(".confirm-modal");
    const confirmationForm = document.querySelector('#confirm-modal-form');
    const header = document.querySelector("header");
    const mainDiv = document.querySelector("#contentDiv");
    const formTitle = document.querySelector('#confirmation-question');

    const split = creatorInfo.split(';');

    if(split[0] === 'questionDeleteConfirmation'){
        formTitle.innerHTML = 'Tem Certeza que Deseja Excluir essa Pergunta?'
        confirmationForm.action = `/question/delete/${split[1]}`;
    }

    if(split[0] === 'commentDeleteConfirmation'){
        formTitle.innerHTML = 'Tem Certeza que Deseja Excluir esse comentário?'
        confirmationForm.action = `/comment/delete/${split[1]}/${split[2]}`;
    }
    if(split[0] === 'answerDeleteConfirmation'){
        formTitle.innerHTML = 'Tem Certeza que Deseja Excluir essa resposta?'
        confirmationForm.action = `/answer/delete/${split[1]}`;
    }

    if(confirmationModal.style.opacity === '1') {
        confirmationModal.style.opacity = '0';
        confirmationModal.style.zIndex = '-99999';
        mainDiv.style.zIndex = '';
        header.style.zIndex = '';
        formTitle.innerHTML = ''
    }
    else{
        confirmationModal.style.opacity = '1';
        confirmationModal.style.zIndex = '99999';
        confirmationModal.style.pointerEvents = 'auto';
        mainDiv.style.zIndex = '-1';
        header.style.zIndex = '-1';
    }
}

function closeConfirmation() {
    const confirmationModal = document.querySelector(".confirm-modal");
    const header = document.querySelector("header");
    const confirmationForm = document.querySelector('#confirm-modal-form');
    const formTitle = document.querySelector('#confirmation-question');
    const mainDiv = document.querySelector("#contentDiv");

    confirmationForm.action = '';
    formTitle.value = '';
    confirmationModal.style.opacity = '0';
    confirmationModal.style.zIndex = '-99999';
    mainDiv.style.zIndex = '';
    confirmationModal.style.pointerEvents = 'none';
    header.style.zIndex = '';
}

function submitConfirmation(){
    document.querySelector('#confirm-modal-form').submit();
}

