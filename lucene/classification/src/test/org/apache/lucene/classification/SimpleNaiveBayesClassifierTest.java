/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.classification;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;
import org.apache.lucene.analysis.reverse.ReverseStringFilter;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.Reader;

/**
 * Testcase for {@link SimpleNaiveBayesClassifier}
 */
// TODO : eventually remove this if / when fallback methods exist for all un-supportable codec methods (see LUCENE-4872)
@LuceneTestCase.SuppressCodecs("Lucene3x")
public class SimpleNaiveBayesClassifierTest extends ClassificationTestBase<BytesRef> {

  @Test
  public void testBasicUsage() throws Exception {
    checkCorrectClassification(new SimpleNaiveBayesClassifier(), TECHNOLOGY_INPUT, TECHNOLOGY_RESULT, new MockAnalyzer(random()), categoryFieldName);
    checkCorrectClassification(new SimpleNaiveBayesClassifier(), POLITICS_INPUT, POLITICS_RESULT, new MockAnalyzer(random()), categoryFieldName);
  }

  @Test
  public void testNGramUsage() throws Exception {
    checkCorrectClassification(new SimpleNaiveBayesClassifier(), TECHNOLOGY_INPUT, TECHNOLOGY_RESULT, new NGramAnalyzer(), categoryFieldName);
  }

  private class NGramAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
      final Tokenizer tokenizer = new KeywordTokenizer(reader);
      return new TokenStreamComponents(tokenizer, new ReverseStringFilter(TEST_VERSION_CURRENT, new EdgeNGramTokenFilter(TEST_VERSION_CURRENT, new ReverseStringFilter(TEST_VERSION_CURRENT, tokenizer), 10, 20)));
    }
  }

}
