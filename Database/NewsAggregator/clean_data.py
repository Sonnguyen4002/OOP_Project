import json

# Define the file paths
file_path = 'news_test.csv'
# new_file_path_delim = 'news.csv'
json_file_path = 'news.json'
final_csv_file_path = 'news.csv'

# Open the file and read its content with the new delimiter "::"
with open(file_path, 'r', encoding='utf-8') as file:
    lines = file.readlines()

# Extract the first line (header)
header = lines[0].strip().split('::')
data_lines = lines[1:]

# Function to find and replace empty values
def replace_empty_values(line, delimiter="::"):
    fields = line.split(delimiter)
    empty_fields = [(i, field) for i, field in enumerate(fields) if not field.strip()]
    for i, field in empty_fields:
        fields[i] = "N/A"
    return delimiter.join(fields), empty_fields

# Filter out lines with more than 12 fields and replace empty values
filtered_lines = []
empty_value_lines = []

for line in data_lines:
    if len(line.split('::')) <= 12:
        new_line, empty_fields = replace_empty_values(line)
        if empty_fields:
            empty_value_lines.append((new_line, empty_fields))
        filtered_lines.append(new_line)

# Print lines with empty values and their positions
print("Lines with empty values and their positions:")
for line, empty_fields in empty_value_lines:
    print(f"Line: {line.strip()}")
    for position, value in empty_fields:
        print(f"  Empty field at position {position}")

# Identify and remove lines with duplicate values in the second field
second_field_seen = set()
unique_second_field_lines = []
duplicate_second_field_lines = []

for line in filtered_lines:
    fields = line.split('::')
    if len(fields) > 1:
        second_field_value = fields[1]
        if second_field_value in second_field_seen:
            duplicate_second_field_lines.append(line)
        else:
            second_field_seen.add(second_field_value)
            unique_second_field_lines.append(line)

# Print duplicate lines in the second field
print("\nDuplicate values in the second field:")
for line in duplicate_second_field_lines:
    print(line.strip())

# Remove duplicate lines and sort the remaining lines
unique_lines = list(set(unique_second_field_lines))
unique_lines.sort()

# Replace the IDs with sequential numbers
modified_lines = [header]  # Start with the header
for index, line in enumerate(unique_lines):
    fields = line.split('::')
    fields[0] = str(index + 1)
    modified_line = '::'.join(fields)
    modified_lines.append(modified_line)

# Remove the last line
if modified_lines:
    modified_lines = modified_lines[:-1]

# Convert to JSON format
data_list = []
for line in modified_lines[1:]:  # Skip the header line for data conversion
    fields = line.strip().split('::')
    data_dict = dict(zip(header, fields))
    data_list.append(data_dict)

# Write the JSON data to a file
with open(json_file_path, 'w', encoding='utf-8') as json_file:
    json.dump(data_list, json_file, ensure_ascii=False, indent=4)

print(f'\nModified JSON file saved to: {json_file_path}')

# Convert JSON back to CSV with "::" delimiter
with open(json_file_path, 'r', encoding='utf-8') as json_file:
    data_list = json.load(json_file)

with open(final_csv_file_path, 'w', encoding='utf-8') as csv_file:
    # Write the header
    csv_file.write('::'.join(header) + '\n')
    # Write the data
    for data_dict in data_list:
        line = '::'.join([str(data_dict[field]) for field in header])
        csv_file.write(line + '\n')

print(f'\nFinal CSV file saved to: {final_csv_file_path}')




# # Define the file paths
# file_path = 'news_test.csv'
# new_file_path_delim = 'news.csv'

# # Open the file and read its content with the new delimiter "::"
# with open(file_path, 'r', encoding='utf-8') as file:
#     lines = file.readlines()

# # Extract the first line (header)
# header = lines[0]
# data_lines = lines[1:]

# # Function to find and replace empty values
# def replace_empty_values(line, delimiter="::"):
#     fields = line.split(delimiter)
#     empty_fields = [(i, field) for i, field in enumerate(fields) if not field.strip()]
#     for i, field in empty_fields:
#         fields[i] = "N/A"
#     return delimiter.join(fields), empty_fields

# # Filter out lines with more than 12 fields and replace empty values
# filtered_lines = []
# empty_value_lines = []

# for line in data_lines:
#     if len(line.split('::')) <= 12:
#         new_line, empty_fields = replace_empty_values(line)
#         if empty_fields:
#             empty_value_lines.append((new_line, empty_fields))
#         filtered_lines.append(new_line)

# # Print lines with empty values and their positions
# print("Lines with empty values and their positions:")
# for line, empty_fields in empty_value_lines:
#     print(f"Line: {line.strip()}")
#     for position, value in empty_fields:
#         print(f"  Empty field at position {position}")

# # Identify and remove lines with duplicate values in the second field
# second_field_seen = set()
# unique_second_field_lines = []
# duplicate_second_field_lines = []

# for line in filtered_lines:
#     fields = line.split('::')
#     if len(fields) > 1:
#         second_field_value = fields[1]
#         if second_field_value in second_field_seen:
#             duplicate_second_field_lines.append(line)
#         else:
#             second_field_seen.add(second_field_value)
#             unique_second_field_lines.append(line)

# # Print duplicate lines in the second field
# print("\nDuplicate values in the second field:")
# for line in duplicate_second_field_lines:
#     print(line.strip())

# # Remove duplicate lines and sort the remaining lines
# unique_lines = list(set(unique_second_field_lines))
# unique_lines.sort()

# # Replace the IDs with sequential numbers
# modified_lines = [header]  # Start with the header
# for index, line in enumerate(unique_lines):
#     fields = line.split('::')
#     fields[0] = str(index + 1)
#     modified_line = '::'.join(fields)
#     modified_lines.append(modified_line)

# # Remove the last line
# if modified_lines:
#     modified_lines = modified_lines[:-1]

# # Write the modified content back to a new file
# with open(new_file_path_delim, 'w', encoding='utf-8') as file:
#     file.writelines(modified_lines)

# print(f'\nModified file saved to: {new_file_path_delim}')