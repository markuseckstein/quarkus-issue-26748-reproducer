package issue26748;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;


@RequestScoped
public class SomeService {

    @Inject JsonWebToken jwt;

    @Inject Logger logger;

    public String getTokenSub() {
        final String jwtSubject = this.jwt.getSubject();
        this.logger.infof("JWT Subject from Service: %s, jwt instance: %s", jwtSubject, this.jwt);
        return jwtSubject;
    }


}
