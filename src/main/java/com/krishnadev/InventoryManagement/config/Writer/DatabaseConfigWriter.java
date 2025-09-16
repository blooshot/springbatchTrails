package com.krishnadev.InventoryManagement.config.Writer;

import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
//@Configuration
public class DatabaseConfigWriter  implements ItemWriter<ProductEntity> {

//    @Bean
//    public JdbcBatchItemWriter<ProductEntity> dbWriter(DataSource dataSource) {
////        return new JdbcBatchItemWriterBuilder<ProductEntity>()
////                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
////                .sql("INSERT INTO products (title, description,instock,quantity, price) VALUES (:title,:description,:instock,:quantity,:price)")
////                .dataSource(dataSource)
////                .build();
////        Connection connection = DataSourceUtils.getConnection(dataSource);
////        getSql(connection);
//        return new JdbcBatchItemWriterBuilder<ProductEntity>()
//                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//                .sql("INSERT INTO products (title, description,instock,quantity, price) VALUES (:title,:description,:instock,:quantity,:price)")
//                .dataSource(dataSource).build();
//    }

    @Override
    public void write(Chunk<? extends ProductEntity> chunk1) throws Exception {
        log.debug("DBCHUNK-Size: {}",chunk1.getItems().size());
        chunk1.getItems().stream().map(x-> (ProductEntity) x)
                .forEach(x-> log.debug("DBCHUNK: {}",x.toString()));

    }

//    private void getSql(Connection connection){
//        String query = "INSERT INTO products (title, description,instock,quantity, price) VALUES (?,?,?,?,?)";
//
//        try {
//            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }


}//EC