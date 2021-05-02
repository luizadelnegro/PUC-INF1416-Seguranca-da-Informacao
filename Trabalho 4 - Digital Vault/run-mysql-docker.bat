docker build -t digital-vault-db .
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=maXAX9pHq4CF -e MYSQL_DATABASE=DigitalVault -e MYSQL_USER=User -e MYSQL_PASSWORD=6GqrPKzrTaSk digital-vault-db