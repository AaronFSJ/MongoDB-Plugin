package com.cybermkd.kit;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.model.*;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:T-baby
 * 创建日期: 16/8/23
 * 文件描述:
 */
public class MongoAggregation {

    private MongoQuery query;
    private List<Bson> pipeline = new ArrayList<Bson>();
    private List<Bson> projections = new ArrayList<Bson>();
    private UnwindOptions unwindOptions = new UnwindOptions();
    private boolean allowDiskUse = true;

    public MongoAggregation(MongoQuery query) {
        this.query = query;
    }

    public void preserveNullAndEmptyArrays(Boolean preserveNullAndEmptyArrays) {
        unwindOptions.preserveNullAndEmptyArrays(preserveNullAndEmptyArrays);
    }

    public MongoAggregation includeArrayIndex(String arrayIndexFieldName) {
        unwindOptions.includeArrayIndex(arrayIndexFieldName);
        return this;
    }

    public MongoAggregation unwind(String field) {
        pipeline.add(Aggregates.unwind(field, unwindOptions));
        return this;
    }

    public MongoAggregation unwind(String field, UnwindOptions unwindOptions) {
        pipeline.add(Aggregates.unwind(field, unwindOptions));
        return this;
    }

    public MongoAggregation match() {
        pipeline.add(Aggregates.match(Filters.and(query.getQuery())));
        return this;
    }

    public MongoAggregation include(String... fieldNames) {
        projections.add(Projections.include(fieldNames));
        return this;
    }

    public MongoAggregation exclude(String... fieldNames) {
        projections.add(Projections.exclude(fieldNames));
        return this;
    }

    public MongoAggregation excludeId() {
        projections.add(Projections.excludeId());
        return this;
    }

    public MongoAggregation sample(int size) {
        pipeline.add(Aggregates.sample(size));
        return this;
    }

    public MongoAggregation sort() {
        pipeline.add(Aggregates.sort(query.getSort()));
        return this;
    }

    public MongoAggregation skip() {
        pipeline.add(Aggregates.skip(query.getSkip()));
        return this;
    }

    public MongoAggregation limit() {
        pipeline.add(Aggregates.limit(query.getLimit()));
        return this;
    }

    public MongoAggregation lookup(String from, String localField, String foreignField, String as) {
        pipeline.add(Aggregates.lookup(from, localField, foreignField, as));
        return this;
    }

    public MongoAggregation out(String collectionName) {
        pipeline.add(Aggregates.out(collectionName));
        return this;
    }

    public MongoAggregation group(String fieldName, MongoAccumulator accumulator) {
        pipeline.add(Aggregates.group(fieldName, accumulator.getAccumulators()));
        return this;
    }

    public MongoAggregation allowDiskUse(boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }

    public List<JSONObject> aggregate() {
        return MongoKit.aggregate(query.getCollectionName(), pipeline, allowDiskUse);
    }

    public <T> List aggregate(Class<T> clazz) {
        return MongoKit.aggregate(query.getCollectionName(), pipeline, allowDiskUse, clazz);
    }


}
