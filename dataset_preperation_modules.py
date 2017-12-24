class RawFeatureToBucketize:
    def __init__(self, feature_name):
        self.feature_name = feature_name
        self.number_of_occurences = 0
        self.score_in_total = 0

    def add_score(self, score_to_add):
        self.number_of_occurences += 1
        self.score_in_total += score_to_add

    def get_average_score(self):
        if self.number_of_occurences == 0:
            return 0
        else:
            return self.score_in_total / self.number_of_occurences

    def get_feature_name(self):
        return self.feature_name


class BucketFeatureExtractor:
    def __init__(self, sentiment_dictionary, features_to_bucketize, sentence_list, score_list):
        self.sentiment_dictionary = sentiment_dictionary
        self.features_to_bucketize = features_to_bucketize
        self.sentence_list = sentence_list
        self.score_list = score_list

    def extract_features(self):
        extracted_features = list()

        for sentence in self.sentence_list:
            number_of_sentiment_words = 0

            number_of_question_marks = 0
            number_of_exclamation_marks = 0
            number_of_negative_smileys = 0
            number_of_positive_smileys = 0
            number_of_very_positive_tokens = 0
            number_of_positive_tokens = 0
            number_of_neutral_tokens = 0
            number_of_minor_negative_tokens = 0
            number_of_negative_tokens = 0
            number_of_very_negative_tokens = 0
            number_of_extremely_negative_tokens = 0
            general_sentiment_weight = 0

            sentence_splitted = sentence.split(' ')

            for word in sentence_splitted:
                if word in self.features_to_bucketize:
                    number_of_sentiment_words += 1
                    general_sentiment_weight += self.features_to_bucketize[word].get_average_score()

                if word == "questionmark":
                    number_of_question_marks += 1
                elif word == "exclamationmark":
                    number_of_exclamation_marks += 1
                elif word == "negativesmiley":
                    number_of_negative_smileys += 1
                elif word == "positivesmiley":
                    number_of_positive_smileys += 1
                elif word in self.sentiment_dictionary:
                    if self.sentiment_dictionary[word] == 'VeryPositive':
                        number_of_very_positive_tokens += 1
                    elif self.sentiment_dictionary[word] == 'Positive':
                        number_of_positive_tokens += 1
                    elif self.sentiment_dictionary[word] == 'Neutral':
                        number_of_neutral_tokens += 1
                    elif self.sentiment_dictionary[word] == 'MinorNegative':
                        number_of_minor_negative_tokens += 1
                    elif self.sentiment_dictionary[word] == 'Negative':
                        number_of_negative_tokens += 1
                    elif self.sentiment_dictionary[word] == 'VeryNegative':
                        number_of_very_negative_tokens += 1
                    elif self.sentiment_dictionary[word] == 'ExtremelyNegative':
                        number_of_extremely_negative_tokens += 1

            feature_list = list()
            feature_list.append(number_of_question_marks)
            feature_list.append(number_of_exclamation_marks)
            feature_list.append(number_of_negative_smileys)
            feature_list.append(number_of_positive_smileys)
            feature_list.append(number_of_very_positive_tokens)
            feature_list.append(number_of_positive_tokens)
            feature_list.append(number_of_neutral_tokens)
            feature_list.append(number_of_minor_negative_tokens)
            feature_list.append(number_of_negative_tokens)
            feature_list.append(number_of_very_negative_tokens)
            feature_list.append(number_of_extremely_negative_tokens)

            if number_of_sentiment_words != 0:
                general_sentiment_weight = general_sentiment_weight / number_of_sentiment_words
            else:
                general_sentiment_weight = 0

            feature_list.append(general_sentiment_weight)

            extracted_features.append(feature_list)

        return extracted_features
