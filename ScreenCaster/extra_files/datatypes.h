/*
 * datatypes.h
 *
 *  Created on: Sep 10, 2013
 *      Author: Piyush
 */

#ifndef DATATYPES_H_
#define DATATYPES_H_

/*
 * Data structures and data types
 * definition follows
 */
typedef struct proc_data {
	//Stores the application file's name
	char app_name[30];

	//Stores all command-line data
	char data[26][100];

	/* Set to 1 to enable
	 * conditional prints printC()
	 * else set to 0
	 * (compiled regardless)
	 */
	int print_status;
}CommandData;

#endif /* DATATYPES_H_ */
