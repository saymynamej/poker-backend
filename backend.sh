container=$(docker ps --filter "name=some-postgres")
if [[ "$container" != *"some"* ]]; then
  docker run --name some-postgres -e POSTGRES_PASSWORD=test -p6789:5432 -d postgres
fi
./gradlew bootRun
