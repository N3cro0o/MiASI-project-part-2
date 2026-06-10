package pl.krasmap.krasnal.infrastructure.out;

import pl.krasmap.common.data.KrasnalCategory;
import pl.krasmap.common.data.Position;
import pl.krasmap.common.data.UpdateTime;
import pl.krasmap.krasnal.application.domain.krasnal.*;
import pl.krasmap.krasnal.application.port.out.KrasnalFetchInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class KrasnalFetchPostgres implements KrasnalFetchInterface {

    private final String postgresAddr = "172.30.144.1:5432";
    private final String postgresString = "jdbc:postgresql://%s/krasnal_db";
    private final String postgresUser = "krasnal_admin";
    private final String postgresPassword = "krasnal";

    private Connection GetDatabaseConnection() throws Exception {
        String targetString = String.format(postgresString, postgresAddr);
        return DriverManager.getConnection(targetString, postgresUser, postgresPassword);
    }

    private List<Krasnal> GetDummyKrasnalObjects() {
        List<Krasnal> list = new ArrayList<>();
        list.add(Krasnal.newObject(0, "test_0", "desc_0", new Position(10.0, 20.0)));
        list.add(Krasnal.newObject(1, "test_1", "desc_1", new Position(11.0, 21.0)));
        list.add(Krasnal.newObject(2, "test_2", "desc_2", new Position(12.0, 22.0)));
        list.add(Krasnal.newObject(3, "test_3", "desc_3", new Position(13.0, 23.0)));
        list.add(Krasnal.newObject(4, "test_4", "desc_4", new Position(14.0, 24.0)));
        return list;
    }

    @Override
    public List<Krasnal> GetAllKrasnalObjects() {
        List<Krasnal> list = null;
        try {
            Connection connection = GetDatabaseConnection();
            Statement statement = connection.createStatement();
            var output = statement.executeQuery("SELECT * FROM poi_catalog.krasnals;");
            list = new ArrayList<>();
            while (output.next()) {
                int id = output.getInt(1);
                String name, desc;
                name = output.getString(2);
                desc = output.getString(3);
                float lat, lon;
                lat = output.getFloat(4);
                lon = output.getFloat(5);
                KrasnalCategory cat = KrasnalCategory.FromString(output.getString(6));
                KrasnalStatus stat = KrasnalStatus.FromString(output.getString(7));
                UpdateTime times;
                OffsetDateTime t1, t2;
                t1 = output.getObject(8, OffsetDateTime.class);
                t2 = output.getObject(9, OffsetDateTime.class);
                times = UpdateTime.from(t1, t2);
                list.add(Krasnal.newObject(id, name, desc, Position.from(lat, lon), cat, stat, times));
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void SaveOrUpdateKrasnal(Krasnal k) {
        System.out.println(k);
    }
}
