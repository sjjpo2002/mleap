package org.apache.spark.ml.mleap.parity.feature

import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.mleap.feature.OneHotEncoder
import org.apache.spark.ml.parity.SparkParityBase
import org.apache.spark.ml.{Pipeline, Transformer}
import org.apache.spark.sql.DataFrame

/**
  * Created by hollinwilkins on 10/30/16.
  */
class OneHotEncoderParitySpec extends SparkParityBase {
  override val dataset: DataFrame = baseDataset.select("state")
  override val sparkTransformer: Transformer = new Pipeline().setStages(Array(new StringIndexer().
    setInputCol("state").
    setOutputCol("state_index"),
    new OneHotEncoder().
      setInputCol("state_index").
      setOutputCol("state_oh"))).fit(dataset)
}
