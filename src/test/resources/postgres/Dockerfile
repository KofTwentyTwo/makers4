FROM postgres:17

# Default credentials for the sample app
ENV POSTGRES_DB=qqq_orders \
    POSTGRES_USER=devuser \
    POSTGRES_PASSWORD=devpass

# Expose standard Postgres port
EXPOSE 5432

# No extra init scripts; Liquibase run from the app will create schema/data.

