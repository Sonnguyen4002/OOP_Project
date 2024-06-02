# Define the file path
file_path = 'news_test.csv'

# Open the file and read its content with the new delimiter "::"
with open(file_path, 'r', encoding='utf-8') as file:
    lines = file.readlines()

# Extract the first line (header)
header = lines[0]
data_lines = lines[1:]

# Create a dictionary to count occurrences of each line
line_counts = {}
for line in data_lines:
    if line in line_counts:
        line_counts[line] += 1
    else:
        line_counts[line] = 1

# Find and print duplicate lines
print("Duplicate lines:")
for line, count in line_counts.items():
    if count > 1:
        print(f"Line: {line.strip()}, Count: {count}")

# Optionally, you can also store these duplicate lines in a list if needed
duplicate_lines = [line for line, count in line_counts.items() if count > 1]

# If you want to print duplicates in a more readable format
print("\nReadable format of duplicates:")
for line in duplicate_lines:
    print(line.strip())

# Save duplicates to a file (optional)
duplicates_file_path = 'duplicate_lines.txt'
with open(duplicates_file_path, 'w', encoding='utf-8') as file:
    file.writelines(duplicate_lines)

print(f"\nDuplicate lines saved to: {duplicates_file_path}")
