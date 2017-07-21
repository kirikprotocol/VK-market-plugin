-- liquibase formatted sql
-- changeset voronov:1

  CREATE SCHEMA IF NOT EXISTS sads_plugin_vk_market
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

  create table cart_items (
        id integer not null auto_increment,
        quantity integer not null,
        vk_item_id integer not null,
        cart_id integer not null,
        primary key (id)
    ) ENGINE=InnoDB default CHARSET=utf8;

    create table carts (
        id integer not null auto_increment,
        primary key (id)
    ) ENGINE=InnoDB; default CHARSET=utf8

    create table order_items (
        id integer not null auto_increment,
        quantity integer not null,
        vk_item_id integer not null,
        order_id integer not null,
        primary key (id)
    ) ENGINE=InnoDB default CHARSET=utf8;

    create table orders (
        id integer not null auto_increment,
        merchant_email varchar(255) not null,
        phone_number varchar(255) not null,
        user_id integer not null,
        primary key (id)
    ) ENGINE=InnoDB default CHARSET=utf8;

    create table users (
        id integer not null auto_increment,
        user_id varchar(255) not null unique,
        cart_id integer not null,
        primary key (id)
    ) ENGINE=InnoDB default CHARSET=utf8;

    alter table cart_items
        add constraint FK_4p7dd2p61wsdx9j35wp6sugqr
        foreign key (cart_id)
        references carts (id);

    alter table order_items
        add constraint FK_9gap2fmw66v092ntb58rtohwh
        foreign key (order_id)
        references orders (id);

    alter table orders
        add constraint FK_k8kupdtcdpqd57b6j4yq9uvdj
        foreign key (user_id)
        references users (id);

    alter table users
        add constraint FK_pnp1baae4enifkkuq2cd01r9l
        foreign key (cart_id)
        references carts (id);

-- rollback ALTER TABLE users DROP FOREIGN KEY FK_pnp1baae4enifkkuq2cd01r9l;
-- rollback ALTER TABLE orders DROP FOREIGN KEY FK_k8kupdtcdpqd57b6j4yq9uvdj;
-- rollback ALTER TABLE order_items DROP FOREIGN KEY FK_9gap2fmw66v092ntb58rtohwh;
-- rollback ALTER TABLE cart_items DROP FOREIGN KEY FK_4p7dd2p61wsdx9j35wp6sugqr;

-- rollback DROP TABLE users;
-- rollback DROP TABLE orders;
-- rollback DROP TABLE order_items;
-- rollback DROP TABLE carts;
-- rollback DROP TABLE cart_items;