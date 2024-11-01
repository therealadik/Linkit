document.addEventListener("DOMContentLoaded", function () {
    const container = document.getElementById('container');
    const signUpButton = document.getElementById('signUp');
    const signInButton = document.getElementById('signIn');
    const signUpOverlayButton = document.getElementById('signUpOverlay');
    const signInOverlayButton = document.getElementById('signInOverlay');

    // Добавляем класс для переключения на форму регистрации
    signUpButton.addEventListener('click', (e) => {
        e.preventDefault();
        container.classList.add("right-panel-active");
    });

    // Убираем класс для переключения на форму авторизации
    signInButton.addEventListener('click', (e) => {
        e.preventDefault();
        container.classList.remove("right-panel-active");
    });

    // Добавляем класс для переключения на форму регистрации из оверлея
    signUpOverlayButton.addEventListener('click', (e) => {
        e.preventDefault();
        container.classList.add("right-panel-active");
    });

    // Убираем класс для переключения на форму авторизации из оверлея
    signInOverlayButton.addEventListener('click', (e) => {
        e.preventDefault();
        container.classList.remove("right-panel-active");
    });
});