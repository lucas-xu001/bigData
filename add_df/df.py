from pyspark.ml import Pipeline
from pyspark.sql import SparkSession
from pyspark.ml.feature import StringIndexer, VectorAssembler
from pyspark.ml.classification import DecisionTreeClassifier
from pyspark.sql.functions import desc

# 初始化 SparkSession，并启用 Hive 支持

warehouse_location = ''
spark = SparkSession.builder \
    .appName("AdRecommendation") \
    .config("spark.sql.warehouse.dir", warehouse_location)\
    .enableHiveSupport() \
    .getOrCreate()

# 从 Hive 仓库中读取广告数据
ads_df = spark.sql("SELECT * FROM your_hive_database.your_ads_table")

# 使用 StringIndexer 将文本列转换为索引
indexers = [StringIndexer(inputCol="product_name", outputCol="product_name_index").fit(ads_df)]

# 使用 VectorAssembler 将所有特征组合为一个特征向量
assembler = VectorAssembler(inputCols=["product_name_index", "product_price", "group_id"],
                            outputCol="features")

# 创建决策树分类器
dt = DecisionTreeClassifier(labelCol="group_id", featuresCol="features")

# 创建 Pipeline
pipeline = Pipeline(stages=indexers + [assembler, dt])

# 拟合模型
model = pipeline.fit(ads_df)

# 从键盘输入新广告信息
new_product_name = input("Enter the product name for the new ad: ")
new_product_price = float(input("Enter the product price for the new ad: "))

# 构建新广告信息字典
new_ad = {'product_name': new_product_name, 'product_price': new_product_price}

# 输入新广告信息，进行预测
new_ad_df = spark.createDataFrame([new_ad])
new_ad_features = assembler.transform(new_ad_df)
prediction = model.transform(new_ad_features)

# 选择点击量最高的广告
top_ad = prediction.select("ad_id", "platform_id", "region_id", "hits").orderBy(desc("hits")).first()

# 输出推荐结果
print("New Ad:")
print(new_ad)
print("Recommended Platform and Region for New Ad:")
print(top_ad.asDict())
