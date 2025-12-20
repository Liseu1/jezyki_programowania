Podczas laboratorium należy zbudować aplikację o przyjaznym, graficznym interfejsie użytkownika, pozwalającą na przeglądanie danych udostępnionych w Internecie poprzez otwarte API. Aplikacja ma być zbudowana z wykorzystaniem klas SWING bądź JavaFX.

Dane powinny pochodzić z portalu GUS, na którym udostępniono parę publicznych API. Zestawienie upublicznionych interfejsów znaleźć można na stronie: https://api.stat.gov.pl/. Podczas realizacji zadania należy skupić się na wybranym zakresie danych Banku Danych Lokalnych, BDL.

Tekstowy opis BDL API można znaleźć na stronie https://api.stat.gov.pl/Home/BdlApi. Opis swaggerowy znajduje się pod adresem: https://api.stat.gov.pl/Home/BdlDocs/1. Opisy BDL API są stosunkowo ubogie. Aby zrozumieć, z czym faktycznie ma się do czynienia, dobrze jest zapoznać się z działaniem aplikacji webowej działającej pod adresem: https://bdl.stat.gov.pl/BDL/start#. Aplikacja ta pozwala na użytkownikowi na przeglądanie różnych zakresów danych, z różnymi kryteriami.

Proszę przestudiować opis API oraz widoki oferowane przez aplikację webową. Następnie proszę wybrać jakiś interesujący zakres danych, jaki będzie renderowany w aplikacji budowanej w ramach laboratorium. Proponuję dane dotyczące jednostek terytorialnej, a dokładniej "Przeciętne wynagrodzenie brutto" czy też "Ludność wg grupy wieku i płci".

Pobieranie danych można zaimplementować na różne sposoby, korzystając z różnych klas dostępnych w JDK oraz w zewnętrznych bibliotekach. Na stronie: https://www.wiremock.io/post/java-http-client-comparison przedstawiono porównanie popularnych klientów, zaś na stronie: https://www.baeldung.com/java-9-http-client opisano, jak użyć HTTPClient (dostępny w JDK).

Parsowanie danych może odbywać się z wykorzystaniem bibliotek do przetwarzania danych w formacie JSON. Krótki tutorial dotyczący tego tematu znajduje się pod adresem: https://www.baeldung.com/java-json.

Proszę zastanowić się nad sposobem wizualizacji. Należy przemyśleć, jak będzie wyglądał interfejs użytkownika (czy użyć tabel, czy też zwykłych pól tekstowych; czy użyć okna dialogowe, czy też zakładki; itp.).

Aplikacja ma być modułowa (w sensie JPMS, ang. Java Platform Module System), a więc powinna posiadać module-info.java z odpowiednimi wpisami. Ponadto powinny pojawić się w pliku pom.xml odpowiednie zależności.

Poza uruchomieniem aplikacji w środowisku programowania będzie trzeba również zaprezentować sposób uruchomienia aplikacji z linii komend. Proszę umieścić komendę uruchomieniową w komentarzach w pliku z klasą główną aplikacji.

Stosunkowo prosto do aplikacji modułowych podłącza się klasy SWING. Trudniej jest z podłączaniem klas JavaFX. Jeśli JavaFX nie jest częścią posiadanego środowiska uruchomieniowego, a stanowi osobny runtime, to wtedy należy odpowiedniej sparametryzować wywołania wirtualnej maszyny (z modyfikacją ścieżki modułów oraz wskazaniem wykorzystanych modułów: --module-path "\path to javafx\lib" --add-modules javafx.controls,javafx.fxml). Przypominam, że runtime Liberica JDK full zawiera JavaFX, więc dla niego nie trzeba ustawiać tych parametrów. Implementując aplikację proszę rozdzielić ją na dwie części, budowane do osobnych plików jar:

* lab04_client - ta część odpowiadać ma za logikę biznesową (wysyłanie zapytań, parsowanie odpowiedzi),
* lab04_gui - ta część odpowiadać ma za graficzny interfejs użytkownika (wizualizuje dane, korzysta z lab04_client).

Proszę zwrócić uwagę na regulamin serwisów. Zwykle pojawiają się w nim jakieś ograniczenia co do liczby wysyłanych zapytań. Jeśli te ograniczenia zostaną złamane, dostarczyciel serwisu może "zbanować" klienta, który wysłał zapytania. Tak więc proszę zachować umiar przy wykonywaniu testów połączeń.
Pozostałe szczegóły mają być zgodne z ustaleniami poczynionymi na początku zajęć.

Pomocnicze linki:
https://www.baeldung.com/java-org-json
https://www.codejava.net/ides/intellij/create-multi-module-maven-project-intellij