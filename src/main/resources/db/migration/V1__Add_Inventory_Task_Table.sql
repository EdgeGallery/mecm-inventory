
    create table applcminventory (
        applcm_id varchar(255) not null,
        tenant_id  varchar(255) not null,
        applcm_ip varchar(255) not null,
        applcm_port varchar(255) not null,
        user_name varchar(255),
        created_time varchar(200),
        modified_time varchar(200),
        primary key (applcm_id)
    );

    create table appstoreinventory (
        appstore_id varchar(255) not null,
        appstore_ip varchar(255) not null,
        appstore_port varchar(255) not null,
        uri varchar(255) not null,
        tenant_id varchar(200) not null,
        user_name varchar(255),
        appstore_name varchar(255),
        producer varchar(255),
        created_time varchar(200),
        modified_time varchar(200),
        primary key (appstore_id)
    );

    create table mechostinventory (
        mechost_id varchar(255) not null,
        tenant_id varchar(255) not null,
        mechost_ip varchar(255) not null,
        applcm_ip varchar(255) not null,
        mechost_name varchar(255) not null,
        zip_code varchar(200),
        city varchar(255) not null,
        address varchar(255) not null,
        affinity varchar(255),
        user_name varchar(255),
        edge_name varchar(255),
        edgerepo_ip varchar(255),
        edgerepo_port varchar(255),
        edgerepo_username varchar(255),
        config_upload_status varchar(255),
        -- config_file_path varchar(2000),
        created_time varchar(200),
        modified_time varchar(200),
        primary key (mechost_id)
    );

    create table tenantinventory (
        tenant_id  varchar(255) not null,
        applcm_count int,
        appstore_count int,
        mechost_count int,
        primary key (tenant_id)
    );