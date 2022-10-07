# Switch to Java later
import random

words = ["test", "words", "overlap", "snake", "stem", "temper", "person", "sonset"]
# words = ["words", "rdsetfds"]

def score(a):
    score = 0
    for i in range(len(a) - 1):
        left = a[i].casefold()
        right = a[i + 1].casefold()

        local_best = 0
        for overlap in range(min(len(left), len(right)) + 1):
            left_slice = left[len(left) - overlap::]
            right_slice = right[:overlap:]
            # print("right slice: {}, left slice: {}".format(right_slice, left_slice))
            if left_slice == right_slice:
                local_best = overlap
        
        score = score + local_best * local_best
    return score

def brute_force(trial_count):
    expendable = words.copy()
    # solution = expendable.copy()
    max = 0
    for i in range(trial_count):
        random.shuffle(expendable)
        this_score = score(expendable)
        if this_score > max:
            max = this_score
            solution = expendable.copy()
    return solution, max

trials = 1000
result, result_score = brute_force(trials)
print(result)
print("Score: {}".format(result_score))

test_score = score(["Stinger", "Gerund", "Underdeveloped", "Eloped", "Pediatric", "trice"])
print("score test: {}".format(test_score))