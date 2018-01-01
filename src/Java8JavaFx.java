import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.junit.Test;

/**第4章 JavaFX
 * @author fengyu .
 * @Date 2018-01-01 15:08
 */
public class Java8JavaFx extends Application {


    //4.2 你好，JavaFX！
    @Test
    public void testHelloJavaFx() {

launch();

    }





    @Override
    public void start(Stage stage) throws Exception {

        Label message = new Label("Hello,JavaFx");
        message.setFont(new Font(100));
        stage.setScene(new Scene(message));

        stage.setTitle("Hello");
        stage.show();


        //    4.3 事件处理
        Button red = new Button("Red");
        red.setOnAction(event ->message.setTextFill(Color.RED));
    }











}
