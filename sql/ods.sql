create database ad;

drop table if exists ods_ads_info_full;
create external table if not exists ods_ads_info_full
(
    id           STRING comment '广告编号',
    product_id   STRING comment '产品id',
    material_id  STRING comment '素材id',
    group_id     STRING comment '广告组id',
    ad_name      STRING comment '广告名称',
    material_url STRING comment '素材地址'
) PARTITIONED BY (`dt` STRING)
    row format delimited fields terminated by '\t'
    LOCATION '/warehouse/ad/ods/ods_ads_info_full';

drop table if exists ods_platform_info_full;
create external table if not exists ods_platform_info_full
(
    id               STRING comment '平台id',
    platform_name_en STRING comment '平台名称(英文)',
    platform_name_zh STRING comment '平台名称(中文)'
) PARTITIONED BY (`dt` STRING)
    row format delimited fields terminated by '\t'
    LOCATION '/warehouse/ad/ods/ods_platform_info_full';

drop table if exists ods_product_info_full;
create external table if not exists ods_product_info_full
(
    id    STRING comment '产品id',
    name  STRING comment '产品名称',
    price decimal(16, 2) comment '产品价格'
) PARTITIONED BY (`dt` STRING)
    row format delimited fields terminated by '\t'
    LOCATION '/warehouse/ad/ods/ods_product_info_full';

drop table if exists ods_ads_platform_full;
create external table if not exists ods_ads_platform_full
(
    id          STRING comment '编号',
    ad_id       STRING comment '广告id',
    platform_id STRING comment '平台id',
    create_time STRING comment '创建时间',
    cancel_time STRING comment '取消时间'
) PARTITIONED BY (`dt` STRING)
    row format delimited fields terminated by '\t'
    LOCATION '/warehouse/ad/ods/ods_ads_platform_full';

drop table if exists ods_server_host_full;
create external table if not exists ods_server_host_full
(
    id   STRING comment '编号',
    ipv4 STRING comment 'ipv4地址'
) PARTITIONED BY (`dt` STRING)
    row format delimited fields terminated by '\t'
    LOCATION '/warehouse/ad/ods/ods_server_host_full';

drop table if exists ods_ad_log_inc;
create external table if not exists ods_ad_log_inc
(
    time_local  STRING comment '日志服务器收到的请求的时间',
    request_method STRING comment 'HTTP请求方法',
    request_uri        STRING comment '请求路径',
    status      STRING comment '日志服务器相应状态',
    server_addr   STRING comment '日志服务器自身ip'
) PARTITIONED BY (`dt` STRING)
    row format delimited fields terminated by '\u0001'
    LOCATION '/warehouse/ad/ods/ods_ad_log_inc';

drop table if exists dim_ads_info_full;
create external table if not exists dim_ads_info_full
(
    ad_id         string comment '广告id',
    ad_name       string comment '广告名称',
    product_id    string comment '广告产品id',
    product_name  string comment '广告产品名称',
    product_price decimal(16, 2) comment '广告产品价格',
    material_id   string comment '素材id',
    material_url  string comment '物料地址',
    group_id      string comment '广告组id'
) PARTITIONED BY (`dt` STRING)
    STORED AS ORC
    LOCATION '/warehouse/ad/dim/dim_ads_info_full'
    TBLPROPERTIES ('orc.compress' = 'snappy');

insert overwrite table dim_ads_info_full partition (dt='2023-01-07')
select
    ad.id,
    ad_name,
    product_id,
    name,
    price,
    material_id,
    material_url,
    group_id
from
(
    select
        id,
        ad_name,
        product_id,
        material_id,
        group_id,
        material_url
    from ods_ads_info_full
    where dt = '2023-01-07'
) ad
left join
(
    select
        id,
        name,
        price
    from ods_product_info_full
    where dt = '2023-01-07'
) pro
on ad.product_id = pro.id;

drop table if exists dim_platform_info_full;
create external table if not exists dim_platform_info_full
(
    id               STRING comment '平台id',
    platform_name_en STRING comment '平台名称(英文)',
    platform_name_zh STRING comment '平台名称(中文)'
) PARTITIONED BY (`dt` STRING)
    STORED AS ORC
    LOCATION '/warehouse/ad/dim/dim_platform_info_full'
    TBLPROPERTIES ('orc.compress' = 'snappy');
select * from dim_ads_info_full;
insert overwrite table dim_platform_info_full partition (dt = '2023-01-07')
select
    id,
    platform_name_en,
    platform_name_zh
from ods_platform_info_full
where dt = '2023-01-07';


drop table if exists dwd_ad_event_inc;
create external table if not exists dwd_ad_event_inc
(
    event_time             bigint comment '事件时间',
    event_type             string comment '事件类型',
    ad_id                  string comment '广告id',
    ad_name                string comment '广告名称',
    ad_product_id          string comment '广告商品id',
    ad_product_name        string comment '广告商品名称',
    ad_product_price       decimal(16, 2) comment '广告商品价格',
    ad_material_id         string comment '广告素材id',
    ad_material_url        string comment '广告素材地址',
    ad_group_id            string comment '广告组id',
    platform_id            string comment '推广平台id',
    platform_name_en       string comment '推广平台名称(英文)',
    platform_name_zh       string comment '推广平台名称(中文)',
    client_country         string comment '客户端所处国家',
    client_area            string comment '客户端所处地区',
    client_province        string comment '客户端所处省份',
    client_city            string comment '客户端所处城市',
    client_ip              string comment '客户端ip地址',
    client_device_id       string comment '客户端设备id',
    client_os_type         string comment '客户端操作系统类型',
    client_os_version      string comment '客户端操作系统版本',
    client_browser_type    string comment '客户端浏览器类型',
    client_browser_version string comment '客户端浏览器版本',
    client_user_agent      string comment '客户端UA',
    is_invalid_traffic     boolean comment '是否是异常流量'
) PARTITIONED BY (`dt` STRING)
    STORED AS ORC
    LOCATION '/warehouse/ad/dwd/dwd_ad_event_inc/'
    TBLPROPERTIES ('orc.compress' = 'snappy');



create temporary table coarse_parsed_log
as
select
    parse_url('http://www.example.com' || request_uri, 'QUERY', 't') event_time,
    split(parse_url('http://www.example.com' || request_uri, 'PATH'), '/')[3] event_type,
    parse_url('http://www.example.com' || request_uri, 'QUERY', 'id') ad_id,
    split(parse_url('http://www.example.com' || request_uri, 'PATH'), '/')[2] platform,
    parse_url('http://www.example.com' || request_uri, 'QUERY', 'ip') client_ip,
    reflect('java.net.URLDecoder', 'decode', parse_url('http://www.example.com'||request_uri,'QUERY','ua'), 'utf-8') client_ua,
    parse_url('http://www.example.com'||request_uri,'QUERY','os_type') client_os_type,
    parse_url('http://www.example.com'||request_uri,'QUERY','device_id') client_device_id
from ods_ad_log_inc
where dt='2023-11-27';

drop function parse_ip;
drop function parse_ua;
create function parse_ip
    as 'com.root.ad.hive.udf.ParseIP'
    using jar 'hdfs://hadoop102:8020//user/hive/jars/ad-hive-udf-parse-ip-1.0-SNAPSHOT-jar-with-dependencies.jar';

create function parse_ua
    as 'com.root.ad.hive.udf.ParseUA'
    using jar 'hdfs://hadoop102:8020//user/hive/jars/ad-hive-udf-parse-ip-1.0-SNAPSHOT-jar-with-dependencies.jar';

select client_ua,
    parse_ua(client_ua)
from coarse_parsed_log;

set hive.vectorized.execution.enabled=false;
create temporary table fine_parsed_log
as
select
    event_time,
    event_type,
    ad_id,
    platform,
    client_ip,
    client_ua,
    client_os_type,
    client_device_id,
    parse_ip('hdfs://hadoop102:8020/ip2region/ip2region.xdb',client_ip) region_struct,
    if(client_ua != '',parse_ua(client_ua),null) ua_struct
from coarse_parsed_log;

drop table if exists dim_crawler_user_agent;
create external table if not exists dim_crawler_user_agent
(
    pattern       STRING comment '正则表达式',
    addition_date STRING comment '收录日期',
    url           STRING comment '爬虫官方url',
    instances     ARRAY<STRING> comment 'UA实例'
)
    STORED AS ORC
    LOCATION '/warehouse/ad/dim/dim_crawler_user_agent'
    TBLPROPERTIES ('orc.compress' = 'snappy');

create temporary table if not exists tmp_crawler_user_agent
(
    pattern       STRING comment '正则表达式',
    addition_date STRING comment '收录日期',
    url           STRING comment '爬虫官方url',
    instances     ARRAY<STRING> comment 'UA实例'
)
    ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.JsonSerDe'
    STORED AS TEXTFILE
    LOCATION '/warehouse/ad/tmp/tmp_crawler_user_agent';

insert overwrite table dim_crawler_user_agent select * from tmp_crawler_user_agent;

create temporary table high_speed_ip
as
select
    distinct client_ip
from
(
    select
        event_time,
        client_ip,
        ad_id,
        count(1) over(partition by client_ip,ad_id order by cast(event_time as bigint) range between 300000 preceding and current row) event_count_last_5min
    from coarse_parsed_log
)t1
where event_count_last_5min>100;

create temporary table cycle_ip
as
select
    distinct client_ip
from
(
    select
        client_ip,
        ad_id,
        s
    from
    (
        select
            event_time,
            client_ip,
            ad_id,
            sum(num) over(partition by client_ip,ad_id order by event_time) s
        from
        (
            select
                event_time,
                client_ip,
                ad_id,
                time_diff,
                if(lag(time_diff,1,0) over(partition by client_ip,ad_id order by event_time)!=time_diff,1,0) num
            from
            (
                select
                    event_time,
                    client_ip,
                    ad_id,
                    lead(event_time,1,0) over(partition by client_ip,ad_id order by event_time)-event_time time_diff
                from coarse_parsed_log
            )t1
        )t2
    )t3
    group by client_ip,ad_id,s
    having count(*)>=5
)t4;

create temporary table high_speed_device
as
select
    distinct client_device_id
from
(
    select
        event_time,
        client_device_id,
        ad_id,
        count(1) over(partition by client_device_id,ad_id order by cast(event_time as bigint) range between 300000 preceding and current row) event_count_last_5min
    from coarse_parsed_log
    where client_device_id != ''
)t1
where event_count_last_5min>100;

create temporary table cycle_device
as
select
    distinct client_device_id
from
(
    select
        client_device_id,
        ad_id,
        s
    from
    (
        select
            event_time,
            client_device_id,
            ad_id,
            sum(num) over(partition by client_device_id,ad_id order by event_time) s
        from
        (
            select
                event_time,
                client_device_id,
                ad_id,
                time_diff,
                if(lag(time_diff,1,0) over(partition by client_device_id,ad_id order by event_time)!=time_diff,1,0) num
            from
            (
                select
                    event_time,
                    client_device_id,
                    ad_id,
                    lead(event_time,1,0) over(partition by client_device_id,ad_id order by event_time)-event_time time_diff
                from coarse_parsed_log
                where client_device_id != ''
            )t1
        )t2
    )t3
    group by client_device_id,ad_id,s
    having count(*)>=5
)t4;

select * from dwd_ad_event_inc;