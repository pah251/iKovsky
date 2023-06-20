package ikovsky;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

public class SongRepository {
    AWSCredentials awsCredentials = new BasicAWSCredentials("AKIASGENHXDSS2NQPRVA", "NLl5QKgU92gXh5czd06xz+oNzT/r4kYYYrkiZNZj");
    AWSStaticCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("eu-west-1").withCredentials(awsCredentialsProvider).build();
    DynamoDB dynamoDB = new DynamoDB(client);

    Table table = dynamoDB.getTable("ikovsky-songs");

    public void submitSongToDynamo(String id, String name, String midiString) {
        Item item = new Item();
        item.withPrimaryKey("song-id", id)
                .withString("midi-value", midiString)
                .withString("song-name", name)
                .withBoolean("saved", false);

        PutItemOutcome outcome = table.putItem(item);

        System.out.println(outcome.getPutItemResult().toString());
    }
}
