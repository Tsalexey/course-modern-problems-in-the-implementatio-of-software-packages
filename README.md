# course-modern-problems-in-the-implementatio-of-software-packages
Набор домашних заданий по предмету "Современные проблемы при реализации комплексов программ"

02.06.01, Компьютерные науки, РУДН, 2019-2020

1. Вычислить разностную производную функции f(x) = e^x, x ∈ [0, 1] тремя способами:

f'|x=xn ≈ (f(x_n+1) − f(x_n))/h, 1 <= n <= N − 1;
f'|x=xn ≈ (f(x_n) − f(x_n−1))/h, 2 <= n <= N;
f'|x=xn ≈ (f(xn+1) − f(xn−1))/2h, 2 <= n <= N − 1.

Здесь h = 1/N – шаг сетки. Для каждого способа провести расчет на нескольких
сетках N = 10, 20, 40, 80, 160, . . . На каждой сетке найти фактическую погрешность

dn = f'точное(x_n) − f'приближенное(x_n), D = max |d_n|.
Построить графики lg D от lg N для каждого способа.


2. Вычислить определенный интеграл I = f(x)dx from 0 to 1, f(x) = e^x двумя способами:

I. sum h/2 * (f(x_n+1) + f(x_n)) from 1 to N-1 (формула трапеций);
II. sum h * f (x_n + h/2) from 1 to N-1 (формула средних).

Для каждого способа провести расчет на нескольких сетках N = 10, 20, 40, 80, 160, . . . Для сеток N = 20, 40, 80, 160, . . . оценить точность расчета по методу Ричардсона DRich и вычислить фактическую погрешность D = |I точное − I приближенное|. Построить график lg DRich и lg D от lg N.


3. Вычислить интеграл I = e^(−x^2)dx from 0 to Inf по формуле средних. Использовать квазиравномерную сетку

x(ξ) = ξ/sqrt(1 + ξ^2), ξ ∈ [0, 1].

Провести исследование сходимости, аналогичное задаче 1. Точное значение интеграла равно I =√π/2.
