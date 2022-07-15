package issue26748;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Uni;

@RequestScoped
@GraphQLApi
public class GraphqlResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    SomeService someService;

    @Inject
    Logger logger;

    @Query("whoami")
    public Uni<ResponseModel> getTokenInfo() {
        return Uni.createFrom().item(() -> {
            final String jwtSubjectFromGraphqlResource = this.jwt.getSubject();
            this.logger.infof("JWT Subject from GraphqlResource: %s, jwt instance: %s", jwtSubjectFromGraphqlResource,
                    this.jwt);

            final String jwtSubjectFromService = this.someService.getTokenSub();

            ResponseModel rm = new ResponseModel();
            rm.jwtSubFromGraphqlResource = jwtSubjectFromGraphqlResource;
            rm.jwtSubFromService = jwtSubjectFromService;

            return rm;
        });

    }

}
