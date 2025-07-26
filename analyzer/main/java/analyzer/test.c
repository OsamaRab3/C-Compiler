#include <stdio.h>
#include <string.h>

float average(int arr[], int size) {
    int sum = 0;
    for (int i = 0; i < size; i++) {
        sum += arr[i];
    }
    return (float)sum / size;
}

int main() {
    int x = 42;
    float y = 3.14;
    char c = 'A';
    char name[20] = "Osama";

    int scores[] = {90, 85, 78, 92, 88};
    int count = sizeof(scores) / sizeof(scores[0]);

    printf("Name: %s\n", name);
    printf("Char: %c, Float: %.2f, Int: %d\n", c, y, x);

    if (x > 10 && y < 10.0) {
        printf("x is greater than 10 and y is less than 10\n");
    } else {
        printf("Condition not met\n");
    }

    for (int i = 0; i < count; i++) {
        printf("Score %d: %d\n", i + 1, scores[i]);
    }

    float avg = average(scores, count);
    printf("Average Score: %.2f\n", avg);

    return 0;
}
