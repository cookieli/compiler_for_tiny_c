.file "tests/codeGenOfMe/if_12.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "ok\n"
.LC2:
.string "wrong\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $64, %rsp
movq $5,-51(%rbp)
movq $10,-43(%rbp)
movq $2,-35(%rbp)
movq -51(%rbp),%rax
cmpq $3,%rax
jle .L1
.L2:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L1:
movq -43(%rbp),%rax
cmpq $8,%rax
jge .L3
movq -35(%rbp),%rax
cmpq $2,%rax
jne .L3
jmp .L2
.L3:
movq $6,-27(%rbp)
movq $7,-19(%rbp)
movq $8,-11(%rbp)
movq -27(%rbp),%rax
movq -19(%rbp),%r10
cmpq %r10,%rax
jge .L4
movq -27(%rbp),%rax
movq -11(%rbp),%r10
cmpq %r10,%rax
jge .L4
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L4
.L4:
movb $1,-3(%rbp)
movb -3(%rbp),%al
cmpb $0,%al
jle .L5
movb $0,-2(%rbp)
jmp .L6
.L5:
movb $1,-2(%rbp)
.L6:
movb $0,-1(%rbp)
movb -3(%rbp),%al
cmpb $0,%al
jle .L7
.L8:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L9
.L7:
movb -2(%rbp),%al
cmpb $0,%al
jle .L10
jmp .L8
.L10:
movb -1(%rbp),%al
cmpb $0,%al
jle .L9
jmp .L8
.L9:
leave
ret
jmp .L4
jmp .L3

