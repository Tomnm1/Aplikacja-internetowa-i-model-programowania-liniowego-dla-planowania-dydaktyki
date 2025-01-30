## Opis projektu
### Backend
- Java 21 (LTS)
- Spring boot - restful - https://spring.io/projects/spring-boot
- dokumentacja: JavaDoc + OpenAPI

### Baza
- PostgreSQL - https://www.postgresql.org/docs/current/index.html, https://jdbc.postgresql.org/documentation/callproc/

### Frontend

- ReactJS https://react.dev
- TailwindCSS https://tailwindcss.com
- https://redux-toolkit.js.org
- https://axios-http.com

### Instrukcja konfiguracji serwera


**Uncomplicated Firewall**
```
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow 22/tcp
sudo ufw allow 443/tcp
sudo ufw enable
sudo ufw logging on
```
Logi w pliku `/var/log/ufw.log`

**Fail2Ban**
```
sudo cp /etc/fail2ban/jail.conf /etc/fail2ban/jail.local
sudo nano /etc/fail2ban/jail.local
```

Znajdź sekcję [sshd] i podmień ją na tą:
```
[sshd]
enabled = true
port    = ssh
filter  = sshd
logpath = /var/log/auth.log
maxretry = 5
bantime = 600
findtime = 600
```
Uruchomienie po zapisanej konfiguracji:
```
sudo systemctl restart fail2ban
sudo systemctl enable fail2ban
sudo fail2ban-client status
```
Powinieneś ujrzeć następujący output:
```
Status
|- Number of jail:    1
`- Jail list:    sshd
```

Sprawdzanie statusu jaila:
`sudo fail2ban-client status sshd`

Unban:
`sudo fail2ban-client set sshd unbanip <ZABLOKOWANE_IP>` - ban analogicznie

Dla zabawy i większego bezpieczeństwa można kiedyś dodać filtrowanie po MAC

**NGINX**
```
sudo apt install nginx
sudo systemctl status nginx
sudo mkdir -p /var/www/hello # dla testów
sudo chown -R $USER:$USER /var/www/hello # niezależnie od strony aby root był właścicielem
```

Dla testów NGINX:
`nano /var/www/hello/index.html`

```
<!DOCTYPE html>
<html>
<head>
    <title>Hello</title>
</head>
<body>
    <h1>Hello, World!</h1>
    <p>NGINX test</p>
</body>
</html>
```

Plik konfiguracyjny:
`sudo nano /etc/nginx/sites-available/hello`
```
server {
    listen 80;
    server_name planner.cs.put.poznan.pl;

    root /var/www/hello;
    index index.html;

    location / {
        try_files $uri $uri/ =404;
    }
}
```
**UWAGA**:  CertBot z nastepnegu punktu sam zajmie się edycją konfiguracji i przepięciem na port 443.

Aktywacja - dowiązanie do pliku
`sudo ln -s /etc/nginx/sites-available/hello /etc/nginx/sites-enabled/`

Skontrolować:
`sudo nginx -t`

W przypadku sukcesu:
```
nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
nginx: configuration file /etc/nginx/nginx.conf test is successful
```
W przypadku porażki NGINX jest świetnie udokumentowany.

Dla zastosowania zmian:
`sudo systemctl restart nginx`

**CERTBOT**

Instalacja:
`sudo apt install certbot python3-certbot-nginx`

Uzyskanie certyfikatu (odnowienie będzie się robiło samo):
`sudo certbot --nginx -d planner.cs.put.poznan.pl`
Przeprowadzi Cię to krok po kroku po request certa.

W przypadku błędu 

```
Certbot failed to authenticate some domains (authenticator: nginx). The Certificate Authority reported these problems:
  Domain: planner.cs.put.poznan.pl
  Type:   connection
  Detail: 150.254.30.238: Fetching http://planner.cs.put.poznan.pl/.well-known/acme-challenge/iCjrNabtBB4PnJ2n-MGD3y0yrfgtGr1oXopp_5M_a9Y: Timeout during connect (likely firewall problem)

Hint: The Certificate Authority failed to verify the temporary nginx configuration changes made by Certbot. Ensure the listed domains point to this nginx server and that it is accessible from the internet.

Some challenges have failed
```
Otwórz port 80 dla NGINX:
`sudo ufw allow 'Nginx Full'`

i ponów 

`sudo certbot --nginx -d planner.cs.put.poznan.pl`

Czy usunięcie tej reguły po wygenerowaniu certa nie zablokuje regeneracje po 90 dniach - wymaga dalszej weryfikacji

### Wgrywanie oprogramowania na serwer:

**Backend:**

Zbudować
`mvn install`

Przekopiować na serwer:
`scp ./planner_endpoints-0.0.1-SNAPSHOT.jar {user}@150.254.30.238:/home/{user}`

Uruchomić w żądany dla siebie sposób, np.
`nohup java -jar planner_endpoints-0.0.1-SNAPSHOT.jar > logs.log 2>&1 &`

Uwaga - polecenie można również zautomatyzować do postaci skryptu powłoki shell uruchamiającej się podczas startu systemu.

**Frontend**

Zbudować:
`yarn force-build`

Przekopiować zawartość katalogu `dist` na serwer:
`scp -r ./dist tkorcz@150.254.30.238:/home/{user}/dist`

Uruchomić skrypt budujący aplikację kliencką:
`rm -fr /var/www/hello/* && mv /home/{user}/dist/dist/* /var/www/hello && chown -R root:root /var/www/hello/* && sudo systemctl reload nginx`
