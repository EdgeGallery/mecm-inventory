
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

    create table mechwcapabilityinventory (
        capability_id varchar(255) not null,
        mechost_id varchar(255) not null,
        tenant_id varchar(255) not null,
        hw_type varchar(200),
        hw_vendor varchar(255),
        hw_model varchar(255),
        created_time varchar(200),
        modified_time varchar(200),
        primary key (capability_id),
        constraint fk_mechost
          foreign key(mechost_id)
        	  references mechostinventory(mechost_id)
    );

    create table mecapplicationinventory (
        appinstance_id varchar(255) not null,
        mechost_id varchar(255) not null,
        tenant_id varchar(255) not null,
        app_name varchar(255) not null,
        package_id varchar(255) not null,
        capabilities varchar(255),
        status varchar(255) not null,
        primary key (appinstance_id),
        constraint fk_mechost_app
          foreign key(mechost_id)
        	  references mechostinventory(mechost_id)
    );

    create table tenantinventory (
        tenant_id  varchar(255) not null,
        applcm_count int,
        appstore_count int,
        mechost_count int,
        mechwcapability_count int,
        mecapplication_count int,
        appdrule_count int,
        appdnsrule_count int,
        apptrafficrule_count int,
        trafficfilter_count int,
        primary key (tenant_id)
    );

    create table appdruleinventory (
        tenant_id varchar(255) not null,
        app_instance_id varchar(255) not null,
        appd_rule_id varchar(255) not null,
        primary key (appd_rule_id)
    );

    create table appdnsruleinventory (
        dns_rule_id varchar(255) not null,
        app_instance_id varchar(255) not null,
        domain_name  varchar(255) not null,
        ip_address_type varchar(255) not null,
        ip_address  varchar(255) not null,
        ttl  varchar(255),
        tenant_id  varchar(255) not null,
        appd_rule_id varchar(255) not null,
        primary key (dns_rule_id),
        constraint fk_appd_dnsrule
          foreign key(appd_rule_id)
            references appdruleinventory(appd_rule_id)
            on delete cascade
    );

    create table apptrafficruleinventory (
        traffic_rule_id varchar(255) not null,
        app_instance_id varchar(255) not null,
        filter_type  varchar(255) not null,
        priority int not null,
        action  varchar(255) not null,
        tenant_id  varchar(255) not null,
        appd_rule_id varchar(255) not null,
        primary key (traffic_rule_id),
        constraint fk_appd_trafficrule
          foreign key(appd_rule_id)
            references appdruleinventory(appd_rule_id)
            on delete cascade
    );

    create table trafficfilterinventory (
        traffic_filter_id varchar(255) not null,
        traffic_rule_id varchar(255) not null,
        src_address array,
        src_port array,
        dst_address array,
        dst_port array,
        protocol array,
        qci int,
        dscp int,
        tc int,
        tenant_id  varchar(255) not null,
        primary key (traffic_filter_id),
        constraint fk_appd_trafficfilter
          foreign key(traffic_rule_id)
            references apptrafficruleinventory(traffic_rule_id)
            on delete cascade
    );

    create table trafficfiltersrcaddressinventory (
        traffic_filter_id varchar(255) not null,
        src_address varchar(255) not null,
        constraint fk_appd_trafficfilter_srcaddress
          foreign key(traffic_filter_id)
            references trafficfilterinventory(traffic_filter_id)
            on delete cascade
    );

    create table trafficfilterdstaddressinventory (
        traffic_filter_id varchar(255) not null,
        dst_address varchar(255) not null,
        constraint fk_appd_trafficfilter_dstaddress
          foreign key(traffic_filter_id)
            references trafficfilterinventory(traffic_filter_id)
            on delete cascade
    );

    create table trafficfiltersrcportinventory (
        traffic_filter_id varchar(255) not null,
        src_port varchar(255) not null,
        constraint fk_appd_trafficfilter_srcport
          foreign key(traffic_filter_id)
            references trafficfilterinventory(traffic_filter_id)
            on delete cascade
    );

    create table trafficfilterdstportinventory (
        traffic_filter_id varchar(255) not null,
        dst_port varchar(255) not null,
        constraint fk_appd_trafficfilter_dstport
          foreign key(traffic_filter_id)
            references trafficfilterinventory(traffic_filter_id)
            on delete cascade
    );

    create table trafficfilterprotocolinventory (
        traffic_filter_id varchar(255) not null,
        protocol varchar(255) not null,
        constraint fk_appd_trafficfilter_protocol
          foreign key(traffic_filter_id)
            references trafficfilterinventory(traffic_filter_id)
            on delete cascade
    );