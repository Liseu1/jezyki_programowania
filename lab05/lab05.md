Podczas laboratorium należy zbudować aplikację, w której dojdzie do synchronizacji wielu wątków. Aplikacja powinna być parametryzowana i pozwalać na uruchamianie wątków oraz obserwowanie ich zachowań i stanów.
Zakładamy, że aplikacja będzie pełnić rolę symulatora stacji przeładunkowej o specyficznej konstrukcji.
Stacja ta posiada

* rampę wejściową o X miejscach,
* tor podajników o Y + 2 * (K - 1) miejscach,
* rampę wyjściową o Y miejscach.

W stacji tej na rampie wejściowej pojawiają się towary, które przeniesione mają być na rampę wyjściową, by następnie z rampy wyjściowej zniknąć. Za funkcjonowanie stacji przeładunkowej odpowiadają wątki:

* Wątek Kreatora - tworzy i dostarcza towary na rampę wejściową na losowe pola. Każdemu towarowi przypisuje losowo wygenerowaną liczbę z zakresu [1,Y].
* Wątki Podajników - przenoszą towary z rampy wejściowej na rampę wyjściową w pola odpowiadające numerom przypisanym do tych towarów. Podajniki pobierają towary z pól bezpośrednio nad sobą i odkładają towary na pola bezpośrednio pod sobą. Podajniki poruszają się po jednym torze znajdującym się między rampami, tak że nie mogą się wyprzedzać. Aby zawsze było możliwe dostarczenie towaru, liczba pól na torze jest odpowiednio większa od liczby pól na rampie wyjściowej (istnieje margines, który pozwala wyjechać części podajników poza rampę wyjściową tak, by podajnik z towarem zaadresowanym do skrajnej pozycji mógł ten towar tam dostarczyć). Dany podajniki może "zobaczyć" towar przenoszony przez podajnik sąsiedni jedynie wtedy, gdy znajdują się w bezpośrednim jego sąsiedztwie (podajnik znajdujący się na polu sąsiadującym z polem zajmowanym przez inny podajnik może zobaczyć towar przenoszony przez ten inny podajnik).
* Wątek Anihilatora - pobiera towary z rampy wyjściowej i je po chwili usuwa.

Wizualizacja stanu wątków chyba najłatwiej zrealizować korzystając z etykiet tekstowych. Mogłaby ona wyglądać tak, jak na schemacie poniżej (X = 2, tzn. są dwa pola rampy wejściowej, zapełniane towarami o numerach w zakresie [1,4]; Y=4, tzn. są cztery pola rampy wyjściowej; K = 3, tzn. są dwa podajniki wizualizowane podkreśleniem - jeśli nic nie przenoszą, lub numerem przenoszonego towaru; Y+2*(K-1) = 8, tzn. tor z podajnikami ma 8 pól)

```text
      |4|1|
| | |_| |_|2| | |
    | | |3| |
```

Można wymyśleć inny sposób wizualizacji zachowania się wątków. Proszę jednak wziąć pod uwagę fakt, iż wizualizacja niekoniecznie pokazywać będzie aktualny stan na planszy. Jeśli np. stan na planszy będzie renderowany na zasadzie odświeżania widoku przez jakiś inny wątek (a nie poprzez reagowanie na każde wygenerowane zdarzenie) może dojść do "przeskoków" (jak w Matrixie).
Proszę pamiętać o odpowiedniej synchronizacji wątków.
Pozostałe szczegóły mają być zgodne z ustaleniami poczynionymi na początku zajęć.