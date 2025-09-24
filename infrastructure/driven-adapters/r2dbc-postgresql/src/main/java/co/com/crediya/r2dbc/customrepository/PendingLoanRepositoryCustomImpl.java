package co.com.crediya.r2dbc.customrepository;


import co.com.crediya.r2dbc.dto.PendingLoanDto;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;


@Repository
public class PendingLoanRepositoryCustomImpl implements PendingLoanRepositoryCustom {


    public static final String QUERY_LOAN = "select sol.monto as amount, sol.plazo as term, sol.email as email, tipo.nombre as name, tipo.tasa_interes as rate, estado.nombre as status " +
            "from solicitud sol " +
            "join tipo_prestamo tipo on tipo.id_tipo_prestamo = sol.id_tipo_prestamo " +
            "join estado estado on estado.id_estado = sol.id_estado " +
            "where sol.id_estado= :status " +
            "and (:email is null or sol.email = :email) ";


    public static final String EMAIL_FIELD = "email";
    public static final String AMOUNT = "amount";
    public static final String TERM = "term";
    public static final String NAME = "name";
    public static final String RATE = "rate";
    public static final String STATUS = "status";
    public static final String SIZE = "size";
    public static final String OFFSET = "offset";


    private DatabaseClient databaseClient;

    public PendingLoanRepositoryCustomImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<PendingLoanDto> findLoansByStatus(int status, String email, int size, int offset) {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(QUERY_LOAN);

        //append pagination when size and offset are provided
        if (size != -1 && offset != -1) {
            queryBuilder.append("LIMIT :size OFFSET :offset");
        }

        var statement = databaseClient.sql(queryBuilder.toString())
                .bind(STATUS, status);


        if (email != null) {
            statement = statement.bind(EMAIL_FIELD, email);
        } else {
            statement = statement.bindNull(EMAIL_FIELD, String.class);
        }

        //use pagination just in case is needed
        if (size != -1 && offset != -1) {
            statement = statement.bind(SIZE, size)
                    .bind(OFFSET, offset);

        }

        return statement
                .map((row, rowMetadata) ->
                        //return the record mapped
                        new PendingLoanDto(
                                row.get(AMOUNT, BigDecimal.class),
                                row.get(TERM, Integer.class),
                                row.get(EMAIL_FIELD, String.class),
                                row.get(NAME, String.class),
                                row.get(RATE, BigDecimal.class),
                                row.get(STATUS, String.class)
                        )
                )
                .all();


    }
}
