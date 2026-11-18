# Calc

Инженерный калькулятор для Android (Kotlin) со сборкой APK через GitHub Actions.

## Возможности

- Арифметика: `+`, `-`, `x`, `/`, `^` (степень)
- Тригонометрия: `sin`, `cos`, `tan`
- Логарифмы: `log`, `ln`
- Корень: `sqrt`, квадрат: `x^2`
- Факториал: `n!`
- Проценты: `%`
- Смена знака: `+/-`
- Память: `M+`, `MR`, `MC`

## Сборка

Сборка запускается автоматически при каждом push в ветку `main` (GitHub Actions, `.github/workflows/android-ci.yml`).
Готовый `app-debug.apk` загружается как артефакт сборки.

## Стек

- Kotlin
- Android SDK (minSdk 24, targetSdk 34)
- Gradle 8.2 / AGP 8.2
