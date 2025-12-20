Podczas laboratorium należy zbudować "mały system", pozwalający na interakcje z użytkownikami (z poziomu konsoli w wersji minimum, z poziomu okienek w wersji rozszerzonej), umożliwiający wykonywanie operacji CRUD (od ang. create, read, update and delete; pol. utwórz, odczytaj, aktualizuj i usuń) na przetwarzanych danych.
Dane powinny być w jakiś sposób utrwalane. Mogą być zapisywane w plikach lub bazie danych zapisywanej do pliku (h2 czy sqlite).
Wymagane jest, by logika biznesowa systemu była oddzielona od interfejsu użytkownika. Ponadto należy obsłużyć własne wyjątki (oprócz wyjątków generowanych przez Java API).

Budowany system powinien wspierać organizację jednodniowych spływów kajakowych. Oczywiście system ten będzie jedynie "przybliżeniem" rzeczywistości. Aby dało się go zaimplementować przyjmujemy znaczące uproszczenia.
Zakładamy, że w procesie biorą udział następujący aktorzy: Klient, Organizator, Pracownik.
Wymienieni aktorzy uzyskują dostęp do systemu za pośrednictwem osobnych aplikacji: ClientApp (oferującej interfejs dla Klienta), OrganizerApp (oferującej interfejs dla Organizatora), EmployeeApp (oferującej interfejs Pracownika).

* Organizator: redaguje ofertę, potwierdza rezerwacje i przekazuje zlecenia do realizacji, aktualizuje status rezerwacji (za pośrednictwem OrganizerApp).
* Klient: przegląda ofertę, zakłada rezerwację, przegląda status rezerwacji (za pośrednictwem ClientApp);
* Pracownik: przegląda zlecenia, aktualizuje status rezerwacji (za pośrednictwem EmployeeApp).

Organizator dysponuje ustaloną liczbą kajaków. Oferta, którą redaguje, zawiera harmonogram z wykazem kilku miejsc, skąd startują spływy. Z każdym terminem i miejscem związany jest górny i dolny limit na liczbę rezerwowanych kajaków. Górny limit jest podawany w ofercie. Dolny limit jest ruchomy.
Organizator na co najmniej dzień przed terminem danego spływu ocenia, czy opłaca mu się wystartować spływ w danym miejscu. Podjęta przez niego decyzja ma swoje odzwierciedlenie w zmianie statusu rezerwacji.
Klient po przeglądnięciu oferty może dokonać rezerwacji. Rezerwacja musi odbyć się przynajmniej dzień przed planowanym terminem rozpoczęcia spływu. Klient powinien sprawdzać status rezerwacji, by nie być zaskoczonym odwołaniem spływu.
Pracownik przyjmuje zlecenia od Organizatora i je realizuje. Zlecenie określa temin, miejsce oraz rezerwacje, jakie mają być obsłużone. Podczas realizacji zlecenia Pracownik zmienia odpowiednio statusy rezerwacji.

Powyższy opis funkcji oferowanych przez poszczególne aplikacje nie jest doskonały. Zrezygnowano w nim z obsługi płatności, rozróżnienia wielkości kajaków, możliwości wypożyczenia dodatkowego sprzętu, jak wiosła czy kamizelki.
Zakres gromadzonych danych w systemie może być minimalny. Poniżej przedstawiono wstępny zarys modelu danych. Model ten należy zmodyfikować odpowiednio do potrzeb. Należy uwzględnić następujące wartości statusu rezerwacji: założona, potwierdzona, realizowana, zrealizowana, odwołana; oraz statusu zlecenia: otwarte, realizowane, zamknięte.

* klient: id, nazwa
* organizator: id, nazwa
* pracownik: id, nazwa
* oferta: id, termin, lokalizacja, max_miejsc, wolnych_miejsc
* rezerwacja: id, id_klienta, id_organizatora, id_oferty, liczba_miejsc, status
* zlecenie: id, id_organizatora, id_pracownika, id_oferty, status

Aby przetestować działanie systemu powinno dać się uruchamiać osobno: przynajmniej jedną instancję OrganizerApp, przynajmniej dwie instancje ClientApp, przynajmniej jedną instancję EmployeeApp. Należy zasymulować upływ czasu. Synchronizacja pomiędzy uruchomionymi instancjami wymienionych aplikacji powinna odbywać się poprzez współdzielenie utrwalanych gdzieś danych. W przypadku zapisywania danych w systemie plików może pojawić się kłopot - system operacyjny może zablokować możliwość zapisu do danego pliku, jeśli aktualnie jest on otwarty w innej aplikacji. Wtedy może przydać się właśnie obsługa wyjątków. Generalnie - implementacja wielodostępu to bardzo trudny temat. Na potrzeby laboratorium mocno go upraszczamy (nie ma potrzeby budowania jakichś bardzo złożonych mechanizmów). W przypadku posługiwania się bazami danych zapisanymi w pliku problem ten może również wystąpić. Pozostałe szczegóły mają być zgodne z ustaleniami poczynionymi na początku zajęć.