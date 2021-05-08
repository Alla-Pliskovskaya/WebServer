#include <iostream>
#include <string>
#include <fstream>

using namespace std;

class block
{
public:
	char* str;
	block* next;
	int length;
	block()
	{
		str = new char[100];
		length = 0;
	}
};

class charList {
public:
	char* array = new char[2];
	int length = 0;


	void add(char a) {
		if (length == sizeof(array)) {
			resize();
		}
		array[length] = a;
		length++;
	}

	void resize() {
		char* newArray = new char[length * 2];
		for (size_t i = 0; i < length; i++)
		{
			newArray[i] = array[i];
		}
		array = newArray;
	}

	bool equals(charList* other) {
		if (other->length != length)
		{
			return false;
		}
		for (size_t i = 0; i < length; i++)
		{
			if (array[i] != other->array[i]) {
				return false;
			}
		}
		return true;
	}
};

class token
{
public:
	charList* chArray;
	int cnt;
	token()
	{
	}

	token(charList* chA)
	{
		chArray = chA;
		cnt = 1;
	}
};

class tokenList {
public:
	token* array = new token[2];
	int length = 0;


	void add(token a) {
		if (length == sizeof(array)) {
			resize();
		}
		array[length] = a;
		length++;
	}

	void resize() {
		token* newArray = new token[length * 2];
		for (size_t i = 0; i < length; i++)
		{
			newArray[i] = array[i];
		}
		array = newArray;
	}

};



int main()
{
	string line;
	block* block1 = new block();
	block* currentBlock = block1;
	int i = 0;
	ifstream in("C:\\Users\\capybara\\source\\repos\\FILP 1\\FILP 1\\1.txt");
	if (in.is_open())
	{
		while (!in.eof())
		{
			char symbol = static_cast<char>(in.get());
			if (i == 100)
			{
				block* block2 = new block();
				block1->next = block2;
				block1 = block2;
				i = 0;
			}
			else
			{
				block1->str[i] = symbol;
				block1->length++;
				i++;
			}
		}
		in.close();
	}
	charList* lst;
	lst = new charList();
	tokenList* dict;
	dict = new tokenList();

	while (currentBlock != nullptr)
	{
		for (size_t i = 0; i < currentBlock->length; i++)
		{
			if (currentBlock->str[i] != ' ' && currentBlock->str[i] != ',' && currentBlock->str[i] != '.') {
				(*lst).add(currentBlock->str[i]);
			}
			else
			{
				bool find = false;
				for (size_t j = 0; j < dict->length; j++)
				{
					if ((*(*dict).array[i].chArray).equals(lst))
					{
						(*dict).array[i].cnt += 1;
						find = true;
						lst = new charList();
						break;
					}
				}
				if (!find)
				{
					dict->add(*(new token(lst)));
					lst = new charList();
				}
			}
		}
	}
	return 0;
}


