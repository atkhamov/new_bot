import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class KaliBotEclipse extends TelegramLongPollingBot{
	public static void main(String[] args) {
        /**
         * First we need to initialize our Api
         */
        ApiContextInitializer.init();

        /**
         * Then we create the Object of Telegram Api
         */
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        /**
         * After that we need to register our Bot
         */
        try{
            telegramBotsApi.registerBot(new KaliBotEclipse());
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
	
	//Create method which receives a message and sends the response
    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();

        /**Turn on the ability of MarkDown*/
        sendMessage.enableMarkdown(true);

        /**It is necessary to explain to Bot, whome to answer by adding the ID*/
        sendMessage.setChatId(message.getChatId().toString());

        /**Identify which message Bot should answer to*/
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try{
            setButtons(sendMessage);
            execute(sendMessage);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }

    }

	public void onUpdateReceived(Update update) {
		Model model = new Model();
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            switch (message.getText()){
                case "/help":
                    sendMsg(message, "How can I help you?");
                    break;
                case "/settings":
                    sendMsg(message, "What shall we set up?");
                    break;
                default:
                    try{
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "The city you entered is not found");
                    }
            }
        }
		
	}
	
	/**Keyboard under the TEXT PANEL*/
    public void setButtons(SendMessage sendMessage){
        //First, we need to create the keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        /**Then we need to create the markup itself*/
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        /**Showing the keyboard to users*/
        replyKeyboardMarkup.setSelective(true);

        /**Keyboard resize based on the screen of the user*/
        replyKeyboardMarkup.setResizeKeyboard(true);

        /**Hide keyboard or not (false - means not to hide the keyboard*/
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        /**NOW WE CREATE THE BUTTONS*/
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        /**Add the messages to the Buttons*/
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/settings"));
        /**Below, we add the keayboardFirstRow to the List named keyboardRowList*/
        keyboardRowList.add(keyboardFirstRow);
        /**Setting up the list into the keyboard*/
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        /**ДАЛЕЕ, ЧТОБЫ НАША КЛАВИАТУРА ЗАРАБОТАЛА, МЫ ДОЛЖНЫ ПОМЕСТИТЬ ЕЕ В ОТПРАВКУ СООБЩЕНИЙ*/
    }

	public String getBotUsername() {
		return "KaliWeatherBot";
	}

	@Override
	public String getBotToken() {
		return "810294930:AAHs1saQQ6zBVoZUTGpcGaDspmiQMNgwc2g";
	}
}
