Podczas laboratorium należy rozwiązać następujący problem optymalizacyjny.

### Założenia:

* W pewnym pomieszczeniu znajduje się regał wnękowy posiadający [n] półek o różnych wysokościach i szerokościach (pomijamy głębokość półki). Wnęka nie musi być prostopadłościanem.
* Istnieje [k] książek, z których każdą można opisać atrybutami: wysokość, grubość, tematyka (pomijamy szerokość książki).

### Zadanie:

* Należy zapełnić regał książkami.
* Podczas zapełniania regału należy rozważyć trzy wartości: wypełnienie półek - mierzone jako suma grubości ułożonych na półkach książek (F - fill), liczba wszystkich ułożonych na półkach książek (C - count), sumaryczna zgodność tematyczna książek ułożonych na jednej półce - mierzona jako suma różnic między liczbą wszystkich tematów a liczbą różnych tematów książek na danej półce (D - difference).

Proszę zauważyć, że jest to problem, w którym funkcję kryterialną można zdefiniować jako sumę ważoną: `wf * F + wc * C + wd * D` (gdzie wagi wf, wc i wd to parametry). Może się okazać, że istnieje kilka sposobów ułożenia książek, dla których wartość kryterium będzie taka sama. Oczywiście na postać rozwiązania będzie miała wpływ postać danych wejściowych.

### Implementacja:

* Dane opisująca problem do rozwiązania przez danego uczestnika konkursu powinny być zapisane w dwóch plikach:
    * `regal.txt` - o zawartości:
        ```text
        # nr półki, wysokość półki, szerokość półki
        1, 15, 40
        2, 10, 40
        ...
        ```
    * `ksiazki.txt` - o zawartości:
        ```text
        # nr książki, wysokość książki, grubość książki, tematyka książki
        1, 13, 10, 3
        2, 15, 9, 4
        ...
        ```
* Wagi kryterium oraz nazwy plików z danymi wejściowymi można przekazać do programu w linii komend lub poprzez interakcje z użytkownikiem po uruchomieniu programu.
* Dane wejściowe należy wygenerować samemu. Dane te powinny być nietrywialne (co do złożoności oraz liczności).

Pozostałe ustalenia omówione zostaną na laboratoriach.