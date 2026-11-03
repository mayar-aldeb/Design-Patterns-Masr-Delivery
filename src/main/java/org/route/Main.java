import org.route.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.route.platform.*;

void main() {

var context = new AnnotationConfigApplicationContext(AppConfig.class);

Platform platform=context.getBean(Platform.class);

    platform.platformMenu();


}
